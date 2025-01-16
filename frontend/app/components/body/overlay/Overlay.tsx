import {DrawingService} from "~/services/DrawingService";
import {observer} from "mobx-react-lite";
import {gameService} from "~/services/GameService";
import RoomStatus from "~/domain/RoomStatus";
import GameSettings from "~/components/body/overlay/GameSettings";
import Waiting from "~/components/body/overlay/Waiting";
import WordSelector from "~/components/body/overlay/WordSelector";

const Overlay = observer(() => {

    const gameStore = gameService.gameStore;

    return (
        <div
            className={"absolute inset-0 p-4 bg-background bg-opacity-75 rounded-large "}
            style={{
                display: gameStore.status !== RoomStatus.IN_TURN ? "block" : "none",
                aspectRatio: DrawingService.CANVAS_ASPECT_RATIO
            }}
        >
            {gameStore.status === RoomStatus.WAITING && gameStore.hosting && (
                <GameSettings/>
            )}
            {
                ((gameStore.status === RoomStatus.WAITING && !gameStore.hosting) ||
                (gameStore.status === RoomStatus.STARTED && !gameStore.drawing)) &&
                (
                    <Waiting/>
                )
            }
            {gameStore.status === RoomStatus.STARTED && gameStore.drawing && gameStore.words.length > 0 && (
                <WordSelector/>
            )}
        </div>
    );
});

export default Overlay;