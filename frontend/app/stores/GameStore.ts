import {makeAutoObservable} from "mobx";
import RoomStatus from "~/domain/RoomStatus";
import type User from "~/domain/User";
import type Message from "~/domain/Message";
import type Word from "~/domain/Word";

const DEFAULT_ROUNDS = 3;
const DEFAULT_DRAWTIME = 80;
const DEFAULT_WORDCOUNT = 3;

export class GameStore {

    // self
    // host: User | null = null;
    // players: User[] | null = null;
    //
    // guessWord: string

    roomCode: string;
    players: User[];
    messages: Message[];

    rounds: number;
    drawtime: number;
    wordCount: number;

    guessWord: string;
    words: Word[];
    currentRound: number;
    status: RoomStatus;
    drawing: boolean;
    hosting: boolean;

    constructor() {
        makeAutoObservable(this);

        this.roomCode = "";
        this.players = [];
        this.messages = [];

        this.rounds = DEFAULT_ROUNDS;
        this.drawtime = DEFAULT_DRAWTIME;
        this.wordCount = DEFAULT_WORDCOUNT;

        this.guessWord = "";
        this.words = [];
        this.currentRound = 0;
        this.status = RoomStatus.UNDEFINED;
        this.drawing = false;
        this.hosting = false;
    }

    setRoomCode(roomCode: string) {
        this.roomCode = roomCode;
    }

    setPlayers(players: User[]) {
        this.players = players;
    }

    setMessages(messages: Message[]) {
        this.messages = messages;
    }

    setRounds(rounds: number) {
        this.rounds = rounds;
    }

    setDrawtime(drawtime: number) {
        this.drawtime = drawtime;
    }

    setWordCount(wordCount: number) {
        this.wordCount = wordCount;
    }

    setStatus(status: RoomStatus) {
        this.status = status;
    }

    setGuessWord(guessWord: string) {
        this.guessWord = guessWord;
    }

    setWords(words: Word[]) {
        this.words = words;
    }

    setCurrentRound(currentRound: number) {
        this.currentRound = currentRound;
    }

    setDrawing(drawing: boolean) {
        this.drawing = drawing;
    }

    setHosting(hosting: boolean) {
        this.hosting = hosting;
    }

    addMessage(message: Message) {
        this.messages.push(message);
    }

}
