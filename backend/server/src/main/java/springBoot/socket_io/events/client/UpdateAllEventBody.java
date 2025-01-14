package springBoot.socket_io.events.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import domain.Room;
import springBoot.socket_io.events.BasicEventBody;

public class UpdateAllEventBody extends BasicEventBody{
    @JsonProperty
    private Room room;

    public UpdateAllEventBody() {
    }

    public UpdateAllEventBody(Room room) {
        this.room = room;
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