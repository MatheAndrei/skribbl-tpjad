package springBoot.socket_io.events.server.body;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import domain.User;
import springBoot.socket_io.events.BasicEventBody;

public class JoinRoomEventBody extends BasicEventBody{
    @JsonProperty
    private String username;
    @JsonProperty
    private String roomId;


    public JoinRoomEventBody() {
    }

    // public JoinRoomEventBody(User sender, String roomId) {
    //     this.sender = sender;
    //     this.roomId = roomId;
    // }
    public JoinRoomEventBody(String username, String roomId) {
        this.username = username;
        this.roomId = roomId;
    }

    @JsonCreator
    public JoinRoomEventBody(String body) {
        ObjectMapper mapper = new ObjectMapper();
        JoinRoomEventBody bodyJson;
        try {
            bodyJson = mapper.readValue(body, JoinRoomEventBody.class);
            this.username = bodyJson.username;
            this.roomId =  bodyJson.roomId;
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "{" +
            " username='" + getUsername() + "'" +
            ", roomId='" + getRoomId() + "'" +
            "}";
    }
}
