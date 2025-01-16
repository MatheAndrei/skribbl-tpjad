import axios from "axios";
import {API_URL, WS_URL} from "~/constants";
import {io, Socket} from "socket.io-client";
import {GameStore} from "~/stores/GameStore";
import type Room from "~/domain/Room";
import RoomStatus from "~/domain/RoomStatus";
import type User from "~/domain/User";
import type Word from "~/domain/Word";
import {drawingService} from "~/services/DrawingService";
import type ToolEnum from "~/services/tools/ToolEnum";

export class GameService {

    private socket: Socket | null;

    me: User;
    roomId: string | null;
    status: RoomStatus;
    image: string;
    tool: ToolEnum | null;

    gameStore: GameStore;
    drawtimeInterval: NodeJS.Timeout | null;

    constructor() {
        this.socket = null;

        this.roomId = null;
        this.me = {
            id: "",
            username: "",
            hasGuessed: false,
            isDrawer: false,
            isHost: false,
            score: 0,
        }
        this.status = RoomStatus.UNDEFINED;
        this.image = "";
        this.tool = null;

        this.gameStore = new GameStore();
        this.drawtimeInterval = null;

        axios.defaults.baseURL = API_URL;
    }

    private registerEvents() {
        if (!this.socket) return;

        this.socket.on("connect", () => this.onConnect());
        this.socket.on("disconnect", () => this.onDisconnect());
        this.socket.on("update_all", (data) => this.onUpdateAll(data));
        this.socket.on("timer_started", () => this.onTimerStarted());
        this.socket.on("timer_up", () => this.onTimerUp());
        this.socket.on("match_over", () => this.onMatchOver());
    }


    // RECEIVE
    private onConnect() {
        if (!this.socket) return;

        this.me.id = this.socket.id!;
    }

    private onDisconnect() {
        console.log("Disconnected");
    }

    private onUpdateAll(data: string) {
        console.log("Update All");

        // get room
        const room: Room = JSON.parse(data)["room"];
        console.log(room);

        // get room id
        this.roomId = room.id;
        this.gameStore.setRoomCode(room.id);

        // get status
        if (room.status !== this.gameStore.status) {
            this.gameStore.setStatus(room.status);
        }

        // get players & me
        this.gameStore.setPlayers(room.players);
        for (const player of room.players) {
            if (player.id === this.me.id) {
                this.me = player;
                this.gameStore.setDrawing(player.isDrawer);
                this.gameStore.setHosting(player.isHost);

                break;
            }
        }

        // get chat
        if ((room.match?.chat ?? []) !== this.gameStore.messages) {
            this.gameStore.setMessages(room.match?.chat ?? []);
        }

        // get word
        if ((room.match?.currentRound.currentTurn?.currentWord?.word ?? "") !== this.gameStore.guessWord) {
            this.gameStore.setGuessWord(room.match?.currentRound.currentTurn?.currentWord?.word ?? "");
        }

        // load image
        if (room.match?.currentRound.currentTurn?.image && !this.me.isDrawer) {
            drawingService.loadImage(room.match?.currentRound.currentTurn?.image.image);

            // const image: {
            //     tool: ToolEnum,
            //     action: string,
            //     size: number,
            //     color: string,
            //     pos: Vec2 | null,
            // } = JSON.parse(room.match?.currentRound.currentTurn?.image.image);
            //
            // drawingService.setSize(image.size);
            // drawingService.setColor(image.color);
            // if (image.tool !== this.tool) {
            //     this.tool = image.tool;
            //     drawingService.setTool(image.tool);
            // }
            //
            // switch (image.action) {
            //     case "clear":
            //         drawingService.clearCanvas();
            //         break;
            //     case "down":
            //         drawingService.selectedTool?.onMouseDown(image.pos!);
            //         break;
            //     case "up":
            //         drawingService.selectedTool?.onMouseUp(image.pos!);
            //         break;
            //     case "move":
            //         drawingService.selectedTool?.onMouseMove(image.pos!);
            //         break;
            // }
        }

        // get settings
        if (room.settings) {
            if (room.settings.numRounds !== this.gameStore.rounds) {
                this.gameStore.setRounds(room.settings.numRounds);
            }
            if (room.settings.timePerTurn !== this.gameStore.drawtime) {
                this.gameStore.setDrawtime(room.settings.timePerTurn);
            }
            if (room.settings.wordCount !== this.gameStore.wordCount) {
                this.gameStore.setWordCount(room.settings.wordCount);
            }
        }

        // get current round
        if ((room.match?.currentRoundNum ?? 0) !== this.gameStore.currentRound) {
            this.gameStore.setCurrentRound(room.match?.currentRoundNum ?? 0);
        }


        // ### FLOW ###


        // show words
        if (room.status === RoomStatus.STARTED && this.me.isDrawer) {
            axios
                .get(`/words/${this.gameStore.wordCount}`)
                .then((res) => {
                    let words: Word[] = res.data;
                    this.gameStore.setWords(words);
                });
        }

    }

    private onTimerStarted() {
        console.log("Timers started");

        // init timer
        this.gameStore.drawtimeCounter = this.gameStore.drawtime;

        // start timer
        this.drawtimeInterval = setInterval(() => {
            this.gameStore.setDrawtimeCounter(this.gameStore.drawtimeCounter - 1);
            if (this.gameStore.drawtimeCounter === 0) {
                if (this.drawtimeInterval !== null) {
                    clearInterval(this.drawtimeInterval);
                }
            }
        }, 1000);
    }

    private onTimerUp() {
        console.log("Timers up");

        if (this.drawtimeInterval !== null) {
            clearInterval(this.drawtimeInterval);
            this.gameStore.setDrawtimeCounter(0);
        }

        this.gameStore.setRevealWord(true);
        setTimeout(() => {
            this.gameStore.setRevealWord(false);
        }, 2500);
    }

    private onMatchOver() {
        console.log("Match over");

        if (this.gameStore.players.length === 0) return;

        const sortedPlayer = this.gameStore.players.slice().sort((a, b) => b.score - a.score);
        const winner = sortedPlayer[0].username;

        this.gameStore.setWinner(winner);
        this.gameStore.setRevealWinner(true);
        setTimeout(() => {
            this.gameStore.setRevealWinner(false);
            this.gameStore.setStatus(RoomStatus.WAITING);
        }, 6000);
    }


    // SEND
    async host(username: string) {
        // create room & get code
        const response = await axios.post("/rooms", {username: username});
        const room = response.data;

        // join the created room
        await this.join(username, room);
    }

    async join(username: string, room: string) {
        this.me.username = username;

        // connect
        this.socket = io(WS_URL, {
            reconnection: false,
            query: {
                username: username
            },
        });

        // register events
        this.registerEvents();

        // join room
        gameService.gameStore = new GameStore();
        const response = await this.socket.emitWithAck("join_room", {
            username: username,
            roomId: room,
        });

        // check if failed
        if (response === "failure") {
            throw new Error(response);
        }

        // update room status
        gameService.gameStore.setStatus(RoomStatus.WAITING);
    }

    async leave() {
        if (!this.socket) return;

        this.socket.disconnect();
    }

    start() {
        axios.put(`/rooms/${this.roomId}`, {
            numRounds: this.gameStore.rounds,
            timePerTurn: this.gameStore.drawtime,
            wordCount: this.gameStore.wordCount,
        });
    }

    sendMessage(message: string) {
        if (!this.socket) return;

        this.socket.emit("message", {
            message: {
                id: "null",
                sender: {
                    id: this.me.id,
                    username: this.me.username,
                    hasGuessed: null,
                    isDrawer: null,
                    isHost: null,
                    score: null,
                    drawer: null
                },
                message: message,
            }
        });
    }

    chooseWord(word: Word) {
        if (!this.socket) return;

        this.socket.emit("choose_word", {
            "sender": {
                "id": this.me.id,
                "username": this.me.username,
                "hasGuessed": null,
                "isDrawer": null,
                "isHost": null,
                "score": null
            },
            "word": {
                "id": word.id,
                "word": word.word
            }
        });

        this.gameStore.setWords([]);
    }

    sendImage(image: string) {
        if (!this.socket) return;

        if (!this.me.isDrawer) return;

        this.socket.emit("draw", {
            "sender": {
                "id": this.me.id,
                "username": this.me.username,
                "hasGuessed": null,
                "isDrawer": null,
                "isHost": null,
                "score": null,
                "drawer": null,
            },
            "image": {
                "image": image,
            },
        });
    }

}


export const gameService = new GameService();
