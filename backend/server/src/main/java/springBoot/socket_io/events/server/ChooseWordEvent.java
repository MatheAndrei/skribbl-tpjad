package springBoot.socket_io.events.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import domain.User;
import domain.Word;
import springBoot.socket_io.events.BasicEvent;
import springBoot.socket_io.events.server.body.ChooseWordEventBody;

public class ChooseWordEvent extends BasicEvent<ChooseWordEventBody> {

    public ChooseWordEvent() {
        super(ServerEventNames.CHOOSE_WORD);
    }

    public ChooseWordEvent(ChooseWordEventBody body) {
        super(ServerEventNames.CHOOSE_WORD, body);
    }

    @JsonCreator
    public ChooseWordEvent(String event) {
        ObjectMapper mapper = new ObjectMapper();
        ChooseWordEvent eventJson;
		try {
			eventJson = mapper.readValue(event, ChooseWordEvent.class);
            this.name = eventJson.name;
            this.body =  eventJson.body;
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }

}
