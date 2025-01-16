import CrownIcon from "~/icons/CrownIcon";
import type User from "~/domain/User";
import {observer} from "mobx-react-lite";
import {gameService} from "~/services/GameService";
import RoomStatus from "~/domain/RoomStatus";
import PencilIcon from "~/icons/PencilIcon";

interface UserProfileProps {
    place: number
    player: User;
}

const PlayerProfile = observer(({place, player}: UserProfileProps) => {

    const gameStore = gameService.gameStore;

    return (
        <div key={player.id} className={"flex justify-between gap-4"}>
            <div>
                <p>#{place}</p>
                {player.isHost && (
                    <CrownIcon fill={"gold"}/>
                )}
            </div>
            <div className={"flex flex-col items-center"}>
                <p className={player.id === gameService.me.id ? "text-primary" : ""}>{player.username}</p>
                <p>{player.score}</p>
            </div>
            <div className={"w-5 h-5 self-center"}>
                {gameStore.status === RoomStatus.IN_TURN && player.isDrawer && (
                    <PencilIcon fill={"brown"} className={"animate-[wiggle_2s_linear_infinite]"}/>
                )}
            </div>
        </div>
    );
});

export default PlayerProfile;