import {Card, CardBody, CardHeader, Divider} from "@nextui-org/react";

function Leaderboard() {
    return (
        <Card className={"w-full"}>
            <CardHeader className={"justify-center text-xl font-bold"}>
                Leaderboard
            </CardHeader>
            <Divider/>
            <CardBody>
                <ul>
                    <li>Player 1</li>
                    <li>Player 2</li>
                    <li>Player 3</li>
                    <li>Player 4</li>
                </ul>
            </CardBody>
        </Card>
    );
}

export default Leaderboard;