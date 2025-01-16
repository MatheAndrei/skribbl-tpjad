import {observer} from "mobx-react-lite";
import {Card, CardBody, CardHeader, Divider} from "@nextui-org/react";
import PlayerProfile from "~/components/leaderboard/PlayerProfile";
import {gameService} from "~/services/GameService";

const Leaderboard = observer(() => {

    const gameStore = gameService.gameStore;
    const players = [...gameStore.players];

    players.sort((a, b) => b.score - a.score);

    return (
        <Card className={"w-48"}>
            <CardHeader>
                <p className={"w-full text-xl text-center font-bold"}>
                    Leaderboard
                </p>
            </CardHeader>
            <Divider/>
            <CardBody>
                <div className={"flex flex-col gap-2"}>
                    {
                        players.map((player, idx) => (
                            <PlayerProfile key={`player-${idx}`} player={player} place={idx + 1}/>
                        ))
                    }
                </div>
            </CardBody>
        </Card>
    );
});

export default Leaderboard;