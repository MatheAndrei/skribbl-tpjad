import {observer} from "mobx-react-lite";
import {gameService} from "~/services/GameService";
import RoomStatus from "~/domain/RoomStatus";

const GuessWord = observer(() => {

    const gameStore = gameService.gameStore;

    const getWord = (): string => {
        if (gameStore.status === RoomStatus.WAITING) {
            return gameStore.roomCode;
        }
        return gameStore.guessWord;
    };

    return (
        <div className={"flex text-xl font-bold"}>

            {/*{word.split("").map((char, idx) => (*/}
            {/*    <div key={`char-${idx}`} className={"w-[1ch] text-center"}>*/}
            {/*        {char}*/}
            {/*    </div>*/}
            {/*))}*/}

            {gameStore.status !== RoomStatus.IN_TURN || gameStore.drawing ? (
                <p className={"text-center"}>{getWord()}</p>
            ) : (
                <p className={"text-center"}>
                    {getWord().split("").map((char) => {
                        if (char !== " ") {
                            return "_";
                        } else {
                            return " ";
                        }
                    })}
                </p>
            )}
        </div>
    );
});

export default GuessWord;