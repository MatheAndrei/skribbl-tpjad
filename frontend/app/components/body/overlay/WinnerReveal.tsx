import {gameService} from "~/services/GameService";
import {observer} from "mobx-react-lite";

const WinnerReveal = observer(() => {

    const gameStore = gameService.gameStore;

    const isActive = (): boolean => {
        return gameStore.revealWinner;
    };

    return (
        <div
            className={"h-full grid place-content-center"}
            style={{
                display: isActive() ? "grid" : "none",
            }}
        >
            <p className={"flex flex-col gap-4 text-2xl text-center"}>
                <span>The winner is</span>
                <span className={"font-bold text-yellow-400"}>{gameStore.winner}</span>
            </p>
        </div>
    );
});

export default WinnerReveal;