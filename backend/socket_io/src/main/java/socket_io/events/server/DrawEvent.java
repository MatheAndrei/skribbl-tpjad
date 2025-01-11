package socket_io.events.server;

import domain.DrawnImage;
import domain.User;
import socket_io.events.BasicEvent;

public class DrawEvent extends BasicEvent {
    
    public class DrawEventBody implements BasicEventBody{
        private User sender;
        private DrawnImage image;
    
        public DrawEventBody() {
        }
    
        public DrawEventBody(User sender, DrawnImage image) {
            this.sender = sender;
            this.image = image;
        }
    
        public User getSender() {
            return this.sender;
        }
    
        public void setSender(User sender) {
            this.sender = sender;
        }
    
        public DrawnImage getImage() {
            return this.image;
        }
    
        public void setImage(DrawnImage image) {
            this.image = image;
        }
    
        @Override
        public String toString() {
            return "{" +
                " sender='" + getSender() + "'" +
                ", image='" + getImage() + "'" +
                "}";
        }
    }

    public DrawEvent() {
        super("draw");
    }

    public DrawEvent(DrawEventBody body) {
        super("draw", body);
    }

}