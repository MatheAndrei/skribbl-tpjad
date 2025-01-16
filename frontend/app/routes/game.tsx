import type { Route } from "./+types/home";
import Header from "~/components/header/Header";
import Leaderboard from "~/components/leaderboard/Leaderboard";
import Chat from "~/components/chat/Chat";
import Footer from "~/components/footer/Footer";
import Body from "~/components/body/Body";
import {useNavigate} from "react-router";
import {gameService} from "~/services/GameService";

export function meta({}: Route.MetaArgs) {
    return [
        { title: "Game" },
    ];
}

function Game() {
    const navigate = useNavigate();

    return (
        <div
            className={"w-full max-h-screen mx-4 grid justify-between content-center gap-2"}
            style={{
                gridTemplateColumns: "min-content auto auto",
                gridTemplateRows: "auto .95fr auto",
                gridTemplateAreas: `
                    ". header header ."
                    "leaderboard body body chat"
                    ". footer footer ."
                `,
            }}
        >
            <div style={{gridArea: "header"}}>
                <Header/>
            </div>
            <div style={{gridArea: "leaderboard"}}>
                <Leaderboard/>
            </div>
            <div style={{gridArea: "body"}}>
                <Body/>
            </div>
            <div style={{gridArea: "chat"}}>
                <Chat/>
            </div>
            <div style={{gridArea: "footer"}}>
                <Footer/>
            </div>
        </div>
    );
}

export default Game;