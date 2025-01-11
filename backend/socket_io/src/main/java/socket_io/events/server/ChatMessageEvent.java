package socket_io.events.server;

import domain.Message;
import socket_io.events.BasicEvent;

public class ChatMessageEvent extends BasicEvent {
    
    public class ChatMessageEventBody implements BasicEventBody{
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

    public ChatMessageEvent() {
        super("message");
    }

    public ChatMessageEvent(ChatMessageEventBody body) {
        super("message", body);
    }

}