import {observer} from "mobx-react-lite";
import {gameService} from "~/services/GameService";
import {ScrollShadow} from "@nextui-org/react";

const ChatMessages = observer(() => {
    return (
        <div className={"flex-1 break-all overflow-y-auto"}>
            <ScrollShadow
                className={"w-full h-full"}
                style={{scrollbarGutter: "stable"}}
            >
                {gameService.gameStore.messages.map((message, idx) => (
                    <p key={`message-${idx}`}>
                        <span className={"mr-1.5 font-bold"}>{message.sender.username}:</span>
                        <span>{message.message}</span>
                    </p>
                ))}
            </ScrollShadow>
        </div>
    );
});

export default ChatMessages;