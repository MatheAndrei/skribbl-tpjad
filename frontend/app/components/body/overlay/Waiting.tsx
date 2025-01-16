import {gameService} from "~/services/GameService";
import RoomStatus from "~/domain/RoomStatus";
import {observer} from "mobx-react-lite";

const Waiting = observer(() => {

    const gameStore = gameService.gameStore;

    const isActive= (): boolean => {
        return (
            !(gameStore.status === RoomStatus.WAITING && gameStore.hosting) &&
            !(gameStore.status === RoomStatus.STARTED && gameStore.drawing) &&
            !(gameStore.status === RoomStatus.IN_TURN) &&
            !(gameStore.revealWord) &&
            !(gameStore.revealWinner)
        );
    };

    return (
        <div
            className={"h-full grid place-content-center"}
            style={{
                display: isActive() ? "grid": "none",
            }}
        >
            <p className={"flex flex-col gap-4 text-2xl text-center"}>
                <span>Waiting...</span>
            </p>
        </div>
    );
});

export default Waiting;