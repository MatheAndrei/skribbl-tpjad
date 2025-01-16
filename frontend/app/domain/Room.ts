import type User from "~/domain/User";
import type Match from "~/domain/Match";
import type RoomStatus from "~/domain/RoomStatus";
import type Settings from "~/domain/Settings";

interface Room {
    id: string;
    host: User;
    players: User[];
    match?: Match;
    status: RoomStatus;
    settings: Settings;
    timer: number;
}

export type {Room as default};
