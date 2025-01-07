package socket_io.events;

import java.util.List;

import com.corundumstudio.socketio.SocketIOClient;

public abstract class BasicEvent {
    protected static String name;
    protected List<SocketIOClient> excludedClients;
    protected BasicEventBody body;

    public interface BasicEventBody {
    
    }

    public BasicEvent(String name) {
        BasicEvent.name = name;
    }

    public BasicEvent(String name, BasicEventBody body) {
        BasicEvent.name = name;
        this.body = body;
    }

    public static String getName() {
        return BasicEvent.name;
    }

    public List<SocketIOClient> getExcludedClients() {
        return this.excludedClients;
    }

    public boolean addExcludedClients(SocketIOClient client) {
        return this.excludedClients.add(client);
    }
    public BasicEventBody getBody() {
        return this.body;
    }
}
