import type Round from "~/domain/Round";
import type Message from "~/domain/Message";

interface Match {
    id: string;
    currentRound: Round;
    chat: Message[];
    currentRoundNum: number;
}

export type {Match as default};
