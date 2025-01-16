import axios from "axios";
import {API_URL, WS_URL} from "~/constants";
import {io, Socket} from "socket.io-client";
import {GameStore} from "~/stores/GameStore";
import type Room from "~/domain/Room";
import RoomStatus from "~/domain/RoomStatus";
import type User from "~/domain/User";
import type Word from "~/domain/Word";
import {drawingService} from "~/services/DrawingService";

export class GameService {

    private socket: Socket | null;

    roomId: string | null;
    me: User;
    status: RoomStatus;

    gameStore: GameStore;

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

        this.gameStore = new GameStore();

        axios.defaults.baseURL = API_URL;
    }

    private registerEvents() {
        if (!this.socket) return;

        this.socket.on("connect", () => this.onConnect());
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

    private onUpdateAll(data: string) {
        console.log("Update All");

        const room: Room = JSON.parse(data)["room"];
        console.log(room);

        this.roomId = room.id;
        this.gameStore.setRoomCode(room.id);

        if (room.status !== this.gameStore.status) {
            this.gameStore.setStatus(room.status);
        }

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
        this.gameStore.setMessages(room.match?.chat ?? []);

        // get word
        this.gameStore.setGuessWord(room.match?.currentRound.currentTurn?.currentWord?.word ?? "");

        // load image
        if (room.match?.currentRound.currentTurn?.image && !this.me.isDrawer) {
            drawingService.loadImage(room.match.currentRound.currentTurn.image.image);
        }

        // get settings
        if (room.settings) {
            this.gameStore.setRounds(room.settings.numRounds);
            this.gameStore.setDrawtime(room.settings.timePerTurn);
            this.gameStore.setWordCount(room.settings.wordCount);
        }

        // get current round
        if (room.match?.currentRound) {
            this.gameStore.setCurrentRound(room.match.currentRoundNum);
        }

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
    }

    private onTimerUp() {
        console.log("Timers up");
    }

    private onMatchOver() {
        console.log("Match over");
        this.gameStore.setStatus(RoomStatus.WAITING);
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

        this.socket.close();
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
