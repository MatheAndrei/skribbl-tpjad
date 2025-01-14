package springBoot.socket_io.events.client;

import springBoot.socket_io.events.BasicEvent;
import springBoot.socket_io.events.BasicEventBody;

public class TimerEndedEvent extends BasicEvent<TimerEndedEventBody>{
    public TimerEndedEvent() {
        super("timer_up");
    }

    public TimerEndedEvent(TimerEndedEventBody body) {
        super("timer_up", body);
    }
}
