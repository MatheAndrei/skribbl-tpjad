import type { Route } from "./+types/home";
import TopBar from "~/components/TopBar";
import Canvas from "~/components/Canvas";
import Leaderboard from "~/components/Leaderboard";
import Chat from "~/components/Chat";
import ToolBar from "~/components/ToolBar";
import ColorBar from "~/components/ColorBar";

export function meta({}: Route.MetaArgs) {
    return [
        { title: "Game" },
    ];
}

function Game() {
    return (
        <div
            className={"w-full mx-4 grid justify-between content-center gap-4"}
            style={{
                gridTemplateColumns: "min-content auto auto",
                // gridTemplateRows: "repeat(4, minmax(250px, 1fr));",
                gridTemplateAreas: `
                    ". header header ."
                    "leaderboard canvas canvas chat"
                    "leaderboard canvas canvas chat"
                    ". toolbar toolbar ."
                    ". colorbar colorbar ."
                `,
            }}
        >
            <div style={{gridArea: "header"}}>
                <TopBar/>
            </div>
            <div style={{gridArea: "leaderboard"}}>
                <Leaderboard/>
            </div>
            <div style={{gridArea: "canvas"}}>
                <Canvas/>
            </div>
            <div style={{gridArea: "chat"}}>
                <Chat/>
            </div>
            <div style={{gridArea: "toolbar"}}>
                <ToolBar/>
            </div>
            <div style={{gridArea: "colorbar"}}>
                <ColorBar/>
            </div>
        </div>
    );
}

export default Game;