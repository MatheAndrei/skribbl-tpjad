package springBoot.socket_io.events.server.body;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import domain.User;
import springBoot.socket_io.events.BasicEventBody;

public class DisconnectEventBody extends BasicEventBody{
    private User sender;

    public DisconnectEventBody() {
    }

    public DisconnectEventBody(User sender) {
        this.sender = sender;
    }

    @JsonCreator
    public DisconnectEventBody(String body) {
        ObjectMapper mapper = new ObjectMapper();
        DisconnectEventBody bodyJson;
        try {
            bodyJson = mapper.readValue(body, DisconnectEventBody.class);
            this.sender = bodyJson.sender;
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public User getSender() {
        return this.sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }


    
    
}
