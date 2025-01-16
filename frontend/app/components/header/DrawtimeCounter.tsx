import {observer} from "mobx-react-lite";
import {gameService} from "~/services/GameService";

const DrawtimeCounter = observer(() => {

    const gameStore = gameService.gameStore;

    return (
        <p>
            <span className={"font-bold"}>Time:</span>
            <span className={"ml-2"}>{gameStore.drawtimeCounter}s</span>
        </p>
    );
});

export default DrawtimeCounter