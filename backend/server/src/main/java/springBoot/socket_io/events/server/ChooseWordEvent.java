package springBoot.socket_io.events.server;

import domain.User;
import domain.Word;
import springBoot.socket_io.events.BasicEvent;

public class ChooseWordEvent extends BasicEvent<ChooseWordEventBody> {

    public ChooseWordEvent() {
        super("choose_word");
    }

    public ChooseWordEvent(ChooseWordEventBody body) {
        super("choose_word", body);
    }

}
