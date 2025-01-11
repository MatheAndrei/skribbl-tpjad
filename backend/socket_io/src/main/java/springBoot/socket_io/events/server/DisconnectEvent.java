package springBoot.socket_io.events.server;

import domain.User;
import springBoot.socket_io.events.BasicEvent;

/// server side
public class DisconnectEvent extends BasicEvent {

    public class DisconnectEventBody implements BasicEventBody{
        private User sender;
    
        public DisconnectEventBody() {
        }
    
        public User getSender() {
            return this.sender;
        }
    
        public void setSender(User sender) {
            this.sender = sender;
        }
    
    
        public DisconnectEventBody(User sender) {
            this.sender = sender;
        }
        
    }

    public DisconnectEvent() {
        super("disconnect");
    }

    public DisconnectEvent(DisconnectEventBody body) {
        super("disconnect", body);
    }

}
