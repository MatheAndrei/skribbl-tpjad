import {observer} from "mobx-react-lite";
import {gameService} from "~/services/GameService";
import {Button} from "@nextui-org/react";
import ToolBar from "~/components/footer/ToolBar";
import ColorBar from "~/components/footer/ColorBar";
import RoomStatus from "~/domain/RoomStatus";

const Footer = observer(() => {

    const gameStore = gameService.gameStore;

    const onStart = () => {
        gameService.start();
    };

    return (
        <>
            {gameStore.status === RoomStatus.WAITING && gameStore.hosting && !gameStore.revealWord && !gameStore.revealWinner && (
                <div className={"grid place-content-center"}>
                    <Button
                        isDisabled={gameStore.players.length < 2}
                        disableRipple
                        color={"success"}
                        size={"lg"}
                        className={"px-12 data-[hover=true]:opacity-100 data-[disabled=true]"}
                        onPress={onStart}
                    >
                        Start
                    </Button>
                </div>
            )}
            {gameStore.status === RoomStatus.IN_TURN && gameStore.drawing && (
                <div className={"flex flex-col gap-4"}>
                    <ToolBar/>
                    <ColorBar/>
                </div>
            )}
        </>
    );
});

export default Footer;