import type { Route } from "./+types/home";
import Canvas from "~/components/Canvas";
import ToolBar from "~/components/ToolBar";
import ColorBar from "~/components/ColorBar";

export function meta({}: Route.MetaArgs) {
    return [
        { title: "Home" },
    ];
}

function Home() {
    return (
        <div style={{
            display: "flex",
            flexDirection: "column",
            gap: 10,
            width: "80%",
            height: 500,
            margin: "10em auto",
        }}>
            <Canvas width={"100%"} height={"100%"}/>
            <div className={"flex justify-between"}>
                <ToolBar/>
                <ColorBar/>
            </div>
        </div>
    );
}

export default Home;