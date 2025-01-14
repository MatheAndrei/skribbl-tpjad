package springBoot.socket_io.events.server;

import domain.Message;
import springBoot.socket_io.events.BasicEvent;

public class ChatMessageEvent extends BasicEvent<ChatMessageEventBody> {

    public ChatMessageEvent() {
        super("message");
    }

    public ChatMessageEvent(ChatMessageEventBody body) {
        super("message", body);
    }

}