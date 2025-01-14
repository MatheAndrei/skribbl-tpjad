package springBoot.socket_io.events.server;

import domain.Message;
import springBoot.socket_io.events.BasicEventBody;

public class ChatMessageEventBody extends BasicEventBody{
    private Message message;

    public ChatMessageEventBody() {
    }

    public ChatMessageEventBody(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return this.message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return "{" +
            " message='" + getMessage() + "'" +
            "}";
    }
}
