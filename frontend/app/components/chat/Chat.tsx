import {Card, CardBody, CardHeader, Divider} from "@nextui-org/react";
import ChatInput from "~/components/chat/ChatInput";
import ChatMessages from "~/components/chat/ChatMessages";

const Chat = (props: any) => {
    return (
        <Card className={"w-full h-full"}>
            <CardHeader className={"justify-center text-xl font-bold"}>
                Chat
            </CardHeader>
            <Divider/>
            <CardBody className={"h-10 flex flex-col gap-3"}>
                <ChatMessages/>
                <ChatInput/>
            </CardBody>
        </Card>
    );
};

export default Chat;