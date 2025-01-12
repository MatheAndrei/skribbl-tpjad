package domain;

import java.util.List;
import java.util.Objects;

public class Match extends BaseEntity<Long>{
    private Round currentRound;
    private List<Message> chat;


    public Match() {
    }

    public Match(Round currentRound, List<Message> chat) {
        this.currentRound = currentRound;
        this.chat = chat;
    }

    public Round getCurrentRound() {
        return this.currentRound;
    }

    public void setCurrentRound(Round currentRound) {
        this.currentRound = currentRound;
    }

    public List<Message> getChat() {
        return this.chat;
    }

    public void setChat(List<Message> chat) {
        this.chat = chat;
    }


    @Override
    public String toString() {
        return "{" +
            " currentRound='" + getCurrentRound() + "'" +
            ", chat='" + getChat() + "'" +
            "}";
    }
    
}
