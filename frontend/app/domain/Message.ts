import type User from "~/domain/User";

interface Message {
    sender: User;
    message: string;
}

export type {Message as default};
