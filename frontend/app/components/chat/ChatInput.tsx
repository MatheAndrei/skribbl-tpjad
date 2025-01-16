import {useRef, useState} from "react";
import {observer} from "mobx-react-lite";
import {gameService} from "~/services/GameService";
import {Button, Input} from "@nextui-org/react";
import SendIcon from "~/icons/SendIcon";
import RoomStatus from "~/domain/RoomStatus";

const ChatInput = observer(() => {

    const gameStore = gameService.gameStore;

    const messageInputRef = useRef<HTMLInputElement>(null);

    const [message, setMessage] = useState<string>("");

    const onMessageChange = (value: string) => setMessage(value);

    const onMessageSend = () => {
        if (!message) return;

        // send message
        gameService.sendMessage(message);

        // clear input message
        setMessage("");

        // focus input message
        if (messageInputRef.current) {
            messageInputRef.current.focus();
        }
    }

    return (
        <Input
            ref={messageInputRef}
            isDisabled={gameStore.status !== RoomStatus.IN_TURN || gameStore.drawing}
            value={message}
            placeholder={"Write your guess..."}
            variant={"faded"}
            size={"lg"}
            endContent={
                <Button
                    isDisabled={gameStore.status !== RoomStatus.IN_TURN || gameStore.drawing}
                    isIconOnly
                    size={"sm"}
                    color={"warning"}
                    onPress={onMessageSend}
                >
                    <SendIcon/>
                </Button>
            }
            onValueChange={onMessageChange}
            onKeyDown={(event) => {
                if (event.key === "Enter") {
                    onMessageSend();
                }
            }}
        />
    );
});

export default ChatInput;