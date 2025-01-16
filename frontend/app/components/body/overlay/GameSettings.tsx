import {type ChangeEvent} from "react";
import {Select, SelectItem} from "@nextui-org/react";
import {observer} from "mobx-react-lite";
import {gameService} from "~/services/GameService";

const allRounds = [2, 3, 4, 5, 6, 7, 8, 9, 10];
const allDrawtime = [15, 20, 30, 40, 50, 60, 70, 80, 90, 100, 120, 150, 180, 210, 240];
const allWordCount = [1, 2, 3, 4, 5];

const GameSettings = observer(() => {

    const onRoundsChange = (event: ChangeEvent<HTMLSelectElement>) => {
        const value = event.target.value;

        if (value) {
            gameService.gameStore.setRounds(parseInt(value));
        }
    };

    const onDrawtimeChange = (event: ChangeEvent<HTMLSelectElement>) => {
        const value = event.target.value;

        if (value) {
            gameService.gameStore.setDrawtime(parseInt(value));
        }
    };

    const onWordCountChange = (event: ChangeEvent<HTMLSelectElement>) => {
        const value = event.target.value;

        if (value) {
            gameService.gameStore.setWordCount(parseInt(value));
        }
    };

    return (
        <div className={"h-full grid grid-cols-2 justify-between content-start items-center gap-4 overflow-y-auto"}>
            <p>Rounds</p>
            <Select
                aria-label={"rounds"}
                selectedKeys={[gameService.gameStore.rounds.toString()]}
                variant={"faded"}
                onChange={onRoundsChange}
            >
                {allRounds.map(round => (
                    <SelectItem key={round}>{round.toString()}</SelectItem>
                ))}
            </Select>

            <p>Drawtime</p>
            <Select
                aria-label={"drawtime"}
                selectedKeys={[gameService.gameStore.drawtime.toString()]}
                variant={"faded"}
                onChange={onDrawtimeChange}
            >
                {allDrawtime.map(time => (
                    <SelectItem key={time}>{time.toString()}</SelectItem>
                ))}
            </Select>

            <p>Word Count</p>
            <Select
                aria-label={"wordcount"}
                selectedKeys={[gameService.gameStore.wordCount.toString()]}
                variant={"faded"}
                onChange={onWordCountChange}
            >
                {allWordCount.map(count => (
                    <SelectItem key={count}>{count.toString()}</SelectItem>
                ))}
            </Select>
        </div>
    );
});

export default GameSettings;