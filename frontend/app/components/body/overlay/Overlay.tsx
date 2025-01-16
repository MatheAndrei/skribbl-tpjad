import {DrawingService} from "~/services/DrawingService";
import {observer} from "mobx-react-lite";
import {gameService} from "~/services/GameService";
import RoomStatus from "~/domain/RoomStatus";
import GameSettings from "~/components/body/overlay/GameSettings";
import Waiting from "~/components/body/overlay/Waiting";
import WordSelector from "~/components/body/overlay/WordSelector";
import WordReveal from "~/components/body/overlay/WordReveal";
import WinnerReveal from "~/components/body/overlay/WinnerReveal";

const Overlay = observer(() => {

    const gameStore = gameService.gameStore;

    const isActive = (): boolean => {
        return gameStore.status !== RoomStatus.IN_TURN;
    };

    return (
        <div
            className={"absolute inset-0 p-4 bg-background bg-opacity-75 rounded-large "}
            style={{
                display: isActive() ? "block" : "none",
                aspectRatio: DrawingService.CANVAS_ASPECT_RATIO
            }}
        >
            <Waiting/>
            <GameSettings/>
            <WordSelector/>
            <WordReveal/>
            <WinnerReveal/>
        </div>
    );
});

export default Overlay;