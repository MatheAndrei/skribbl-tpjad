import {useNavigate} from "react-router";
import {observer} from "mobx-react-lite";
import {gameService} from "~/services/GameService";
import {Button, Card, CardBody} from "@nextui-org/react";
import GuessWord from "~/components/header/GuessWord";
import RoomStatus from "~/domain/RoomStatus";

const Header = observer(() => {
    const navigate = useNavigate();

    const gameStore = gameService.gameStore;

    const waiting = true;
    const guessWord = "Guess Word";

    const onLeave = () => {
        gameService
            .leave()
            .then(() => navigate("/"))
            .catch((error) => console.log(error));
    };

    return (
        <Card>
            <CardBody className={"flex flex-row justify-between items-center gap-3"}>
                <div className={"flex-1 flex flex-wrap gap-5 text-xl"}>
                    <p>
                        <span className={"font-bold"}>Round: </span>
                        <span className={"ml-2"}>
                            {gameStore.status === RoomStatus.WAITING && "∞"}
                            {gameStore.status !== RoomStatus.WAITING && gameStore.currentRound + "/" + gameStore.rounds}
                        </span>
                    </p>
                    <p>
                        <span className={"font-bold"}>Time:</span>
                        <span className={"ml-2"}>56s</span>
                    </p>
                </div>
                <GuessWord
                    word={
                        gameStore.status === RoomStatus.WAITING
                            ? gameStore.roomCode
                            : gameStore.guessWord
                    }
                    hide={gameStore.status === RoomStatus.IN_TURN && !gameStore.drawing}
                />
                <div className={"flex-1 flex justify-end"}>
                    <Button
                        color={"danger"}
                        onPress={onLeave}
                    >
                        Leave
                    </Button>
                </div>
            </CardBody>
        </Card>
    );
});

export default Header;