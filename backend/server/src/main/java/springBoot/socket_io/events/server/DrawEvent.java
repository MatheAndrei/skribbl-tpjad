package springBoot.socket_io.events.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import domain.DrawnImage;
import domain.User;
import springBoot.socket_io.events.BasicEvent;
import springBoot.socket_io.events.server.body.DrawEventBody;

public class DrawEvent extends BasicEvent<DrawEventBody> {

    public DrawEvent() {
        super(ServerEventNames.DRAW);
    }

    public DrawEvent(DrawEventBody body) {
        super(ServerEventNames.DRAW, body);
    }

    @JsonCreator
    public DrawEvent(String event) {
        ObjectMapper mapper = new ObjectMapper();
        DrawEvent eventJson;
		try {
			eventJson = mapper.readValue(event, DrawEvent.class);
            this.name = eventJson.name;
            this.body =  eventJson.body;
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }
}