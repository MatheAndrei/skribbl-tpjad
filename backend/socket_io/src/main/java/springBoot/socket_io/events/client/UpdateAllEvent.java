package springBoot.socket_io.events.client;

import domain.Room;
import springBoot.socket_io.events.BasicEvent;

public class UpdateAllEvent extends BasicEvent {
    
    public class UpdateAllEventBody implements BasicEventBody{
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

    public UpdateAllEvent() {
        super("update_all");
    }

    public UpdateAllEvent(UpdateAllEvent.UpdateAllEventBody body) {
        super("update_all", body);
    }

}
