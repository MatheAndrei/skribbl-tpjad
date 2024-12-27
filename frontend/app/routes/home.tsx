import type { Route } from "./+types/home";

export function meta({}: Route.MetaArgs) {
    return [
        { title: "Home" },
    ];
}

function Home() {
    return (
        <p>It works!</p>
    );
}

export default Home;