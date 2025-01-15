package springBoot.socket_io.events.client.body;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import domain.Room;
import springBoot.socket_io.events.BasicEventBody;
import springBoot.socket_io.events.server.body.DrawEventBody;

public class UpdateAllEventBody extends BasicEventBody{
    @JsonProperty
    private Room room;

    public UpdateAllEventBody() {
    }

    public UpdateAllEventBody(Room room) {
        this.room = room;
    }

    @JsonCreator
    public UpdateAllEventBody(String body) {
        ObjectMapper mapper = new ObjectMapper();
        UpdateAllEventBody bodyJson;
        try {
            bodyJson = mapper.readValue(body, UpdateAllEventBody.class);
            this.room = bodyJson.room;
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public Room getRoom() {
        return this.room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "{" +
            " room='" + getRoom() + "'" +
            "}";
    }
}