package springBoot.socket_io.events;

import java.util.ArrayList;
import java.util.List;

import com.corundumstudio.socketio.SocketIOClient;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import springBoot.socket_io.events.server.JoinRoomEvent;

public class BasicEvent<T extends BasicEventBody> {
    @JsonProperty
    protected String name;
    protected List<SocketIOClient> excludedClients;
    @JsonProperty
    protected T body;

    public BasicEvent() {
        excludedClients = new ArrayList<>();
    }

    public BasicEvent(String name) {
        this.name = name;
        excludedClients = new ArrayList<>();
    }

    public BasicEvent(String name, T body) {
        this.name = name;
        this.body = body;
        excludedClients = new ArrayList<>();
    }

    public String getName() {
        return this.name;
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

    // @JsonCreator
    // public BasicEvent<E extends BasicEvent>(E event) {
    //     ObjectMapper mapper = new ObjectMapper();
    //     JoinRoomEvent eventJson;
	// 	try {
	// 		eventJson = mapper.readValue(event, E.class);
    //         this.name = eventJson.name;
    //         this.body =  eventJson.body;
	// 	} catch (JsonMappingException e) {
	// 		e.printStackTrace();
	// 	} catch (JsonProcessingException e) {
	// 		e.printStackTrace();
	// 	}
    // }
}
