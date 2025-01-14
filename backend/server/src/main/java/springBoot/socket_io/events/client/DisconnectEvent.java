package springBoot.socket_io.events.client;

import springBoot.socket_io.events.BasicEvent;
import springBoot.socket_io.events.BasicEventBody;

/// client side
public class DisconnectEvent extends BasicEvent<BasicEventBody> {

    public DisconnectEvent() {
        super("disconnect");
    }

    public DisconnectEvent(DisconnectEventBody body) {
        super("disconnect", body);
    }

}
