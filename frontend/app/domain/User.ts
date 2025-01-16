interface User {
    id: string;
    username: string;
    hasGuessed: boolean;
    isDrawer: boolean;
    isHost: boolean;
    score: number;
}

export type {User as default};
