import {Button, Card, CardBody, CardHeader, Divider, Input} from "@nextui-org/react";
import SendIcon from "~/icons/SendIcon";

function Chat() {
    return (
        <Card className={"w-full h-full"}>
            <CardHeader className={"justify-center text-xl font-bold"}>
                Chat
            </CardHeader>
            <Divider/>
            <CardBody className={"flex flex-col gap-4"}>
                <div className={"flex-1 flex flex-col-reverse"}>
                    <p><strong>Player 2:</strong> Lorem ipsum dolor</p>
                    <p><strong>Player 1:</strong> Lorem ipsum dolor</p>
                    <p><strong>Player 1:</strong> Lorem ipsum dolor</p>
                </div>
                <Input
                    label={""}
                    placeholder={"Write your guess..."}
                    variant={"faded"}
                    size={"lg"}
                    endContent={
                        <Button isIconOnly type={"button"} size={"sm"} color={"warning"}>
                            <SendIcon/>
                        </Button>
                    }
                />
            </CardBody>
        </Card>
    );
}

export default Chat;