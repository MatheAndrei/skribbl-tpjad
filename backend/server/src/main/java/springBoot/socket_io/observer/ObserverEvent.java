package springBoot.socket_io.observer;
import java.util.Objects;

public class ObserverEvent {
    private ObserverEventTypes eventType;
    private Object body;


    public ObserverEvent() {
    }

    public ObserverEvent(ObserverEventTypes eventType, Object body) {
        this.eventType = eventType;
        this.body = body;
    }

    public ObserverEventTypes getEventType() {
        return this.eventType;
    }

    public void setEventType(ObserverEventTypes eventType) {
        this.eventType = eventType;
    }

    public Object getBody() {
        return this.body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public ObserverEvent eventType(ObserverEventTypes eventType) {
        setEventType(eventType);
        return this;
    }

    public ObserverEvent body(Object body) {
        setBody(body);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ObserverEvent)) {
            return false;
        }
        ObserverEvent observerEvent = (ObserverEvent) o;
        return Objects.equals(eventType, observerEvent.eventType) && Objects.equals(body, observerEvent.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventType, body);
    }

    @Override
    public String toString() {
        return "{" +
            " eventType='" + getEventType() + "'" +
            ", body='" + getBody() + "'" +
            "}";
    }
    
}
