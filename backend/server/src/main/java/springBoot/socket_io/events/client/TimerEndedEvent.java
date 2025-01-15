package springBoot.socket_io.events.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import springBoot.socket_io.events.BasicEvent;
import springBoot.socket_io.events.client.body.TimerEndedEventBody;

public class TimerEndedEvent extends BasicEvent<TimerEndedEventBody>{
    public TimerEndedEvent() {
        super(ClientEventNames.TIMER_ENDED);
    }

    public TimerEndedEvent(TimerEndedEventBody body) {
        super(ClientEventNames.TIMER_ENDED, body);
    }

    @JsonCreator
    public TimerEndedEvent(String event) {
        ObjectMapper mapper = new ObjectMapper();
        TimerEndedEvent eventJson;
		try {
			eventJson = mapper.readValue(event, TimerEndedEvent.class);
            this.name = eventJson.name;
            this.body =  eventJson.body;
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }
}
