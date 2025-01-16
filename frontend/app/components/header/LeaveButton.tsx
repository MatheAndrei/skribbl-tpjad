import {Button} from "@nextui-org/react";
import {useNavigate} from "react-router";
import {gameService} from "~/services/GameService";

const LeaveButton = () => {

    const navigate = useNavigate();

    const onLeave = () => {
        gameService
            .leave()
            .then(() => navigate("/"))
            .catch((error) => console.log(error));
    };

    return (
        <Button
            color={"danger"}
            onPress={onLeave}
        >
            Leave
        </Button>
    );
};

export default LeaveButton;