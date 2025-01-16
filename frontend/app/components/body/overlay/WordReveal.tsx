import {gameService} from "~/services/GameService";
import {observer} from "mobx-react-lite";

const WordReveal = observer(() => {

    const gameStore = gameService.gameStore;

    const isActive = (): boolean => {
        return gameStore.revealWord;
    }

    return (
        <div
            className={"h-full grid place-content-center"}
            style={{
                display: isActive() ? "grid" : "none",
            }}
        >
            <p className={"flex flex-col gap-4 text-2xl text-center"}>
                <span>The word was</span>
                <span className={"font-bold text-primary"}>{gameStore.previousGuessWord}</span>
            </p>
        </div>
    );
});

export default WordReveal;