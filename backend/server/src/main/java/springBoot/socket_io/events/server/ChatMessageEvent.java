package springBoot.socket_io.events.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import domain.Message;
import springBoot.socket_io.events.BasicEvent;
import springBoot.socket_io.events.server.body.ChatMessageEventBody;

public class ChatMessageEvent extends BasicEvent<ChatMessageEventBody> {

    public ChatMessageEvent() {
        super(ServerEventNames.CHAT_MESSAGE);
    }

    public ChatMessageEvent(ChatMessageEventBody body) {
        super(ServerEventNames.CHAT_MESSAGE, body);
    }

    @JsonCreator
    public ChatMessageEvent(String event) {
        ObjectMapper mapper = new ObjectMapper();
        ChatMessageEvent eventJson;
		try {
			eventJson = mapper.readValue(event, ChatMessageEvent.class);
            this.name = eventJson.name;
            this.body =  eventJson.body;
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }

}