import type User from "~/domain/User";
import type Word from "~/domain/Word";
import type DrawnImage from "~/domain/DrawnImage";

interface Turn {
    id: string;
    drawUser: User;
    currentWord?: Word;
    image: DrawnImage;
}

export type {Turn as default};
