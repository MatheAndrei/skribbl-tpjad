import {Button, Card, CardBody} from "@nextui-org/react";

function TopBar() {
    return (
        <Card>
            <CardBody className={"flex flex-row justify-between items-center"}>
                <p><strong>Time:</strong> 0s</p>
                <div>
                    <p><strong>_ _ _ _ _</strong></p>
                </div>
                <Button type={"button"} color={"danger"}>
                    Leave
                </Button>
            </CardBody>
        </Card>
    );
}

export default TopBar;