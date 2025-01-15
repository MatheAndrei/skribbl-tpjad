package springBoot.socket_io.events.server.body;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import domain.User;
import domain.Word;
import springBoot.socket_io.events.BasicEventBody;

public class ChooseWordEventBody extends BasicEventBody{
    private User sender;
    private Word word;

    public ChooseWordEventBody() {
    }

    public ChooseWordEventBody(User Sender, Word word) {
        this.sender = Sender;
        this.word = word;
    }

    @JsonCreator
    public ChooseWordEventBody(String body) {
        ObjectMapper mapper = new ObjectMapper();
        ChooseWordEventBody bodyJson;
        try {
            bodyJson = mapper.readValue(body, ChooseWordEventBody.class);
            this.sender = bodyJson.sender;
            this.word =  bodyJson.word;
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public User getSender() {
        return this.sender;
    }

    public void setSender(User Sender) {
        this.sender = Sender;
    }

    public Word getWord() {
        return this.word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return "{" +
            " Sender='" + getSender() + "'" +
            ", word='" + getWord() + "'" +
            "}";
    }
}