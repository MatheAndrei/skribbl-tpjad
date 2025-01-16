import type Turn from "~/domain/Turn";

interface Round {
    id: string;
    currentTurn?: Turn;
    turns: Turn[];
}

export type {Round as default};
