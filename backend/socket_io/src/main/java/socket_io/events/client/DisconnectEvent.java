package socket_io.events.client;

import socket_io.events.BasicEvent;

/// client side
public class DisconnectEvent extends BasicEvent{

    public class DisconnectEventBody implements BasicEventBody{

    }

    public DisconnectEvent() {
        super("disconnect");
    }

    public DisconnectEvent(DisconnectEventBody body) {
        super("disconnect", body);
    }

}
