package springBoot.socket_io.events.server.body;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import domain.Message;
import springBoot.socket_io.events.BasicEventBody;

public class ChatMessageEventBody extends BasicEventBody{
    private Message message;

    public ChatMessageEventBody() {
    }

    public ChatMessageEventBody(Message message) {
        this.message = message;
    }

    @JsonCreator
    public ChatMessageEventBody(String body) {
        ObjectMapper mapper = new ObjectMapper();
        ChatMessageEventBody bodyJson;
        try {
            bodyJson = mapper.readValue(body, ChatMessageEventBody.class);
            this.message = bodyJson.message;
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public Message getMessage() {
        return this.message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return "{" +
            " message='" + getMessage() + "'" +
            "}";
    }
}
