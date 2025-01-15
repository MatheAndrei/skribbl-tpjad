package springBoot.socket_io.events.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import springBoot.socket_io.events.BasicEvent;
import springBoot.socket_io.events.BasicEventBody;
import springBoot.socket_io.events.client.body.DisconnectEventBody;

/// client side
public class DisconnectEvent extends BasicEvent<BasicEventBody> {

    public DisconnectEvent() {
        super(ClientEventNames.DISCONNECT);
    }

    public DisconnectEvent(DisconnectEventBody body) {
        super(ClientEventNames.DISCONNECT, body);
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
