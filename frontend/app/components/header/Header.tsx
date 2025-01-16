import {observer} from "mobx-react-lite";
import {Card, CardBody} from "@nextui-org/react";
import GuessWord from "~/components/header/GuessWord";
import RoundCounter from "~/components/header/RoundCounter";
import DrawtimeCounter from "~/components/header/DrawtimeCounter";
import LeaveButton from "~/components/header/LeaveButton";

const Header = observer(() => {
    return (
        <Card>
            <CardBody className={"flex flex-row justify-between items-center gap-3"}>
                <div className={"flex-1 flex flex-wrap gap-5 text-xl"}>
                    <RoundCounter/>
                    <DrawtimeCounter/>
                </div>
                <GuessWord/>
                <div className={"flex-1 flex justify-end"}>
                    <LeaveButton/>
                </div>
            </CardBody>
        </Card>
    );
});

export default Header;