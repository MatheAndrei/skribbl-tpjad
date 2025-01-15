package springBoot.socket_io.events.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import springBoot.socket_io.events.BasicEvent;
import springBoot.socket_io.events.client.body.UpdateAllEventBody;

public class UpdateAllEvent extends BasicEvent<UpdateAllEventBody> {
    public UpdateAllEvent() {
        super(ClientEventNames.UPDATE_ALL);
    }

    public UpdateAllEvent(UpdateAllEventBody body) {
        super(ClientEventNames.UPDATE_ALL, body);
    }

    @JsonCreator
    public UpdateAllEvent(String event) {
        ObjectMapper mapper = new ObjectMapper();
        UpdateAllEvent eventJson;
		try {
			eventJson = mapper.readValue(event, UpdateAllEvent.class);
            this.name = eventJson.name;
            this.body =  eventJson.body;
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }

}
