package springBoot.socket_io.events.server;

import domain.User;
import springBoot.socket_io.events.BasicEvent;

/// server side
public class DisconnectEvent extends BasicEvent<DisconnectEventBody> {

    public DisconnectEvent() {
        super("disconnect");
    }

    public DisconnectEvent(DisconnectEventBody body) {
        super("disconnect", body);
    }

}
