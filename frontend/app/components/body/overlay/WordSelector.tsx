import {Button} from "@nextui-org/react";
import {observer} from "mobx-react-lite";
import {gameService} from "~/services/GameService";
import type Word from "~/domain/Word";

const WordSelector = observer(() => {

    const gameStore = gameService.gameStore;

    const onWordSelected = (word: Word) => {
        gameService.chooseWord(word);
    }

    return (
        <div className={"h-full flex flex-col justify-center items-center gap-2"}>
            <p className={"pb-4 text-2xl"}>
                Choose a word
            </p>
            {gameStore.words.map((word, idx) => (
                <Button
                    key={`word-${idx}`}
                    variant={"faded"}
                    className={"w-1/"}
                    onPress={() => onWordSelected(word)}
                >
                    {word.word}
                </Button>
            ))}
        </div>
    );
});

export default WordSelector;