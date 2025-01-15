package springBoot.socket_io.events.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import springBoot.socket_io.events.BasicEvent;
import springBoot.socket_io.events.client.body.TimerEndedEventBody;
import springBoot.socket_io.events.client.body.TimerStartedEventBody;

public class MatchEndedEvent extends BasicEvent<TimerStartedEventBody>{
    public MatchEndedEvent() {
        super(ClientEventNames.MATCH_ENDED);
    }

    public MatchEndedEvent(TimerStartedEventBody body) {
        super(ClientEventNames.MATCH_ENDED, body);
    }

    @JsonCreator
    public MatchEndedEvent(String event) {
        ObjectMapper mapper = new ObjectMapper();
        MatchEndedEvent eventJson;
        try {
            eventJson = mapper.readValue(event, MatchEndedEvent.class);
            this.name = eventJson.name;
            this.body =  eventJson.body;
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}