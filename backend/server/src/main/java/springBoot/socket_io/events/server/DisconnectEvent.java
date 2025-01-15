package springBoot.socket_io.events.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import domain.User;
import springBoot.socket_io.events.BasicEvent;
import springBoot.socket_io.events.server.body.DisconnectEventBody;

/// server side
public class DisconnectEvent extends BasicEvent<DisconnectEventBody> {

    public DisconnectEvent() {
        super(ServerEventNames.DISCONNECT);
    }

    public DisconnectEvent(DisconnectEventBody body) {
        super(ServerEventNames.DISCONNECT, body);
    }

    @JsonCreator
    public DisconnectEvent(String event) {
        ObjectMapper mapper = new ObjectMapper();
        DisconnectEvent eventJson;
		try {
			eventJson = mapper.readValue(event, DisconnectEvent.class);
            this.name = eventJson.name;
            this.body =  eventJson.body;
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }

}
