package springBoot.socket_io.events.server.body;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import domain.DrawnImage;
import domain.User;
import springBoot.socket_io.events.BasicEventBody;

public class DrawEventBody extends BasicEventBody{
    private User sender;
    private DrawnImage image;

    public DrawEventBody() {
    }

    public DrawEventBody(User sender, DrawnImage image) {
        this.sender = sender;
        this.image = image;
    }

    @JsonCreator
    public DrawEventBody(String body) {
        ObjectMapper mapper = new ObjectMapper();
        DrawEventBody bodyJson;
        try {
            bodyJson = mapper.readValue(body, DrawEventBody.class);
            this.sender = bodyJson.sender;
            this.image =  bodyJson.image;
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
