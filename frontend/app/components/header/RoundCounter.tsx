import {observer} from "mobx-react-lite";
import {gameService} from "~/services/GameService";
import RoomStatus from "~/domain/RoomStatus";

const RoundCounter = observer(() => {

    const gameStore = gameService.gameStore;

    return (
        <p>
            <span className={"font-bold"}>Round: </span>
            <span className={"w-[3ch] ml-2"}>
                {gameStore.status === RoomStatus.WAITING && "∞"}
                {gameStore.status !== RoomStatus.WAITING && (
                    (gameStore.currentRound + 1) + "/" + gameStore.rounds
                )}
            </span>
        </p>
    );
});

export default RoundCounter;