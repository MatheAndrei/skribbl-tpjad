package springBoot.socket_io.events.server;

import domain.User;
import springBoot.socket_io.events.BasicEventBody;

public class DisconnectEventBody extends BasicEventBody{
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
