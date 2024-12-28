import type { Route } from "./+types/home";
import Canvas from "~/components/Canvas";

export function meta({}: Route.MetaArgs) {
    return [
        { title: "Home" },
    ];
}

function Home() {
    return (
        <Canvas width={500} height={500} canvasColor={"black"}/>
    );
}

export default Home;