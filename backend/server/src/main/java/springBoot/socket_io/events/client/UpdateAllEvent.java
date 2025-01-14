package springBoot.socket_io.events.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import domain.Room;
import springBoot.socket_io.events.BasicEvent;


public class UpdateAllEvent extends BasicEvent<UpdateAllEventBody> {
    public UpdateAllEvent() {
        super("update_all");
    }

    public UpdateAllEvent(UpdateAllEventBody body) {
        super("update_all", body);
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
