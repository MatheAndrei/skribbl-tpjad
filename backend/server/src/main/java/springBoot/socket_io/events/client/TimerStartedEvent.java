package springBoot.socket_io.events.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import springBoot.socket_io.events.BasicEvent;
import springBoot.socket_io.events.client.body.TimerEndedEventBody;
import springBoot.socket_io.events.client.body.TimerStartedEventBody;

public class TimerStartedEvent extends BasicEvent<TimerStartedEventBody>{
    public TimerStartedEvent() {
        super(ClientEventNames.TIMER_STARTED);
    }

    public TimerStartedEvent(TimerStartedEventBody body) {
        super(ClientEventNames.TIMER_STARTED, body);
    }

    @JsonCreator
    public TimerStartedEvent(String event) {
        ObjectMapper mapper = new ObjectMapper();
        TimerStartedEvent eventJson;
        try {
            eventJson = mapper.readValue(event, TimerStartedEvent.class);
            this.name = eventJson.name;
            this.body =  eventJson.body;
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}