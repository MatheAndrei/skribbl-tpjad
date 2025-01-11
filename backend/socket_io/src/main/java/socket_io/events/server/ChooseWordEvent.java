package socket_io.events.server;

import domain.User;
import domain.Word;
import socket_io.events.BasicEvent;

public class ChooseWordEvent extends BasicEvent {
    
    public class ChooseWordEventBody implements BasicEventBody{
        private User sender;
        private Word word;
    
        public ChooseWordEventBody() {
        }
    
        public ChooseWordEventBody(User Sender, Word word) {
            this.sender = Sender;
            this.word = word;
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

    public ChooseWordEvent() {
        super("choose_word");
    }

    public ChooseWordEvent(ChooseWordEventBody body) {
        super("choose_word", body);
    }

}
