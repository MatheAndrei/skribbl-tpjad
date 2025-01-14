package springBoot.socket_io.events.server;

import domain.DrawnImage;
import domain.User;
import springBoot.socket_io.events.BasicEvent;

public class DrawEvent extends BasicEvent<DrawEventBody> {

    public DrawEvent() {
        super("draw");
    }

    public DrawEvent(DrawEventBody body) {
        super("draw", body);
    }

}