package springBoot.socket_io.events.server;

import domain.DrawnImage;
import domain.User;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import springBoot.socket_io.events.BasicEvent;
import springBoot.socket_io.events.BasicEventBody;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//@Jacksonized @Builder
public class JoinRoomEvent extends BasicEvent<JoinRoomEventBody>{
    public JoinRoomEvent() {
        super("join_room");
    }

    public JoinRoomEvent(JoinRoomEventBody body) {
        super("join_room", body);
    }

    @JsonCreator
    public JoinRoomEvent(String event) {
        ObjectMapper mapper = new ObjectMapper();
        JoinRoomEvent eventJson;
		try {
			eventJson = mapper.readValue(event, JoinRoomEvent.class);
            this.name = eventJson.name;
            this.body =  eventJson.body;
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }
}



