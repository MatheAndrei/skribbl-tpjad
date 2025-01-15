package springboot.socket_io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import domain.Room;
import domain.RoomSettings;
import domain.User;
import domain.Word;
import domain.enums.RoomStatus;
import springBoot.socket_io.SessionService;
import springBoot.socket_io.SocketIOConfiguration;
import springBoot.socket_io.SocketIOService;
import springBoot.socket_io.events.server.ChooseWordEvent;
import springBoot.socket_io.events.server.JoinRoomEvent;
import springBoot.socket_io.events.server.body.ChooseWordEventBody;
import springBoot.socket_io.events.server.body.JoinRoomEventBody;
import springBoot.socket_io.observer.ObserverEventTypes;
import springboot.ObservableTest;

@SpringBootTest
@ActiveProfiles("test")
public class TestSocketIoService {
    @Value("${socket-server.host}")
    private String testingHost;
    @Value("${socket-server.port}")
    private Integer testingPort;
    @Autowired
    private SessionService serviceS;



    @BeforeEach
    void resetService(){
        this.serviceS.reset();
    }

    @Test
    void testCreateUser(){
        String testUsername= "test";
        User user = serviceS.createUser(testUsername);

        assertNull(user.getId());
        assertEquals(user.getUsername(), testUsername);
        assertNull(user.hasGuessed());
        assertNull(user.isDrawer());
        assertNull(user.isIsHost());
    } 

    @Test
    void testCreateRoom(){
        String testUsername= "test";
        User user = serviceS.createUser(testUsername);
        Room room = this.serviceS.createRoom(user);

        assertNotNull(room.getId());
        assertEquals(room.getHost(), user);
        assertEquals(room.getStatus(),RoomStatus.Undefined);
        assertEquals(room.getPlayers().size(),0);
    }


    @Test
    void testConnection() throws InterruptedException, URISyntaxException {
        String testUsername = "aaa";
        SocketIOClientTest client = new SocketIOClientTest(testingHost, testingPort);
        client.connect(testUsername);
    
        Thread.sleep(1000); 
        assertTrue(client.isConnected(), "Client failed to connect.");
    }

    @Test
    void testConnectionLinkHost() throws InterruptedException, URISyntaxException {
        String testUsername = "aaa";
        User user = this.serviceS.createUser(testUsername);
        SocketIOClientTest client = new SocketIOClientTest(testingHost, testingPort);
        client.connect(testUsername);
        Thread.sleep(1000); 

        user = this.serviceS.getUserByUsername(testUsername);
        
        assertTrue(client.isConnected(), "Client failed to connect.");
        assertNotNull(user.getId());
    }

    @Test
    void testConnectionLinkNonHost() throws InterruptedException, URISyntaxException {
        String testUsername = "aaa";
        SocketIOClientTest client = new SocketIOClientTest(testingHost, testingPort);
        client.connect(testUsername);
        Thread.sleep(1000); 

        User user = this.serviceS.getUserByUsername(testUsername);
        
        assertTrue(client.isConnected(), "Client failed to connect.");
        assertNotNull(user.getId());
    }

    @Test
    void testConnectionJoinHostRoom() throws InterruptedException, URISyntaxException {
        String testUsername = "aaa";
        User userH = this.serviceS.createUser(testUsername);
        Room room = this.serviceS.createRoom(userH);

        SocketIOClientTest client = new SocketIOClientTest(testingHost, testingPort);
        client.connect(testUsername);
        Thread.sleep(1000); 

        boolean result = this.serviceS.joinRoom(userH, room.getId());
        User userHostS = this.serviceS.getUserByUsername(testUsername);
        
        assertTrue(client.isConnected(), "Client failed to connect.");
        assertTrue(userHostS.getIsHost());
        assertTrue(result);
    }

    @Test
    void testConnectionJoinNonHost() throws InterruptedException, URISyntaxException {
        String testUsernameHost = "aaa";
        ///host
        User userH = this.serviceS.createUser(testUsernameHost);
        SocketIOClientTest clientH = new SocketIOClientTest(testingHost, testingPort);
        clientH.connect(testUsernameHost);
        Thread.sleep(1000); 
        Room room = this.serviceS.createRoom(userH);
        boolean result = this.serviceS.joinRoom(userH, room.getId());

        ///non host
        String testUsernameNonHost = "bbb";
        User userNH = this.serviceS.createUser(testUsernameNonHost);
        SocketIOClientTest clientNH = new SocketIOClientTest(testingHost, testingPort);
        clientNH.connect(testUsernameNonHost);
        Thread.sleep(1000); 
        result = this.serviceS.joinRoom(userNH, room.getId());

        User userHostS = this.serviceS.getUserByUsername(testUsernameHost);
        User userNonHostS = this.serviceS.getUserByUsername(testUsernameNonHost);
        
        room = this.serviceS.getRoomById(room.getId());

        assertTrue(clientH.isConnected(), "Client Host failed to connect.");
        assertTrue(clientNH.isConnected(), "Client Non Host failed to connect.");
        assertTrue(userHostS.getIsHost());
        assertFalse(userNonHostS.getIsHost());
        assertFalse(userNonHostS.getIsHost());
        assertEquals(room.getPlayers().size(), 2);
        assertTrue(result);
    }

    @Test
    /// does not work properly due to a 10y old bug in socketio client implementation for java, client does not listen fro signals
    void testConnectionJoinHostRoomSignal() throws InterruptedException, URISyntaxException {
        String testUsername = "aaa";
        User userH = this.serviceS.createUser(testUsername);
        Room room = this.serviceS.createRoom(userH);

        SocketIOClientTest client = new SocketIOClientTest(testingHost, testingPort);
        client.connect(testUsername);
        Thread.sleep(1000); 

        JoinRoomEvent event = new JoinRoomEvent(new JoinRoomEventBody(userH, room.getId()));
        String eventJson = "" ;
        try {
            eventJson = new ObjectMapper().writeValueAsString(event.getBody());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(eventJson);

        client.clientSocket.emit(new JoinRoomEvent().getName(), eventJson);
        if (client.latch.await(10, TimeUnit.SECONDS)) {
            System.out.println("Update all event data ");
        } else {
            System.out.println("Timeout waiting for the event!");
        }
        //Thread.sleep(2000); 
        User userHostS = this.serviceS.getUserByUsername(testUsername);
        
        assertTrue(client.isConnected(), "Client failed to connect.");
        assertTrue(userHostS.getIsHost());
        //assertNotNull(client.room);
        assertTrue(userHostS.getIsHost());
    }

    @Test
    void testLeaveClientRoomEmpty() throws InterruptedException, URISyntaxException {
        String testUsername = "aaa";
        User userH = this.serviceS.createUser(testUsername);
        Room room = this.serviceS.createRoom(userH);

        SocketIOClientTest client = new SocketIOClientTest(testingHost, testingPort);
        client.connect(testUsername);
        Thread.sleep(1000); 

        boolean result = this.serviceS.joinRoom(userH, room.getId());
        var clientIOServer = this.serviceS.getClientByUsername(testUsername);

        result = this.serviceS.removeClient(clientIOServer);
        userH = this.serviceS.getUserByUsername(testUsername);
        room = this.serviceS.getRoomById(room.getId());

        assertTrue(result);
        assertNull(userH.getId());
        assertNull(room.getId());
    }

    @Test
    void testRemoveUserNonExistent(){
        String testUsernameNonHost = "testNH";
        User user = serviceS.createUser(testUsernameNonHost);

        boolean result = this.serviceS.removeUser(user);

        assertFalse(result);
    }

    @Test
    void testLeaveClientRoomStillExists() throws InterruptedException, URISyntaxException {
        String testUsernameHost = "aaa";
        ///host
        User userH = this.serviceS.createUser(testUsernameHost);
        SocketIOClientTest clientH = new SocketIOClientTest(testingHost, testingPort);
        clientH.connect(testUsernameHost);
        Thread.sleep(1000); 
        Room room = this.serviceS.createRoom(userH);
        boolean result = this.serviceS.joinRoom(userH, room.getId());
        var clientIOServerH = this.serviceS.getClientByUsername(testUsernameHost);

        ///non host
        String testUsernameNonHost = "bbb";
        User userNH = this.serviceS.createUser(testUsernameNonHost);
        SocketIOClientTest clientNH = new SocketIOClientTest(testingHost, testingPort);
        clientNH.connect(testUsernameNonHost);
        Thread.sleep(1000); 
        result = this.serviceS.joinRoom(userNH, room.getId());
        var clientIOServerNH = this.serviceS.getClientByUsername(testUsernameNonHost);

        result = this.serviceS.removeClient(clientIOServerNH);

        var userNHServer = this.serviceS.getUserByUsername(testUsernameNonHost);
        room = this.serviceS.getRoomById(room.getId());
        User userHostS = this.serviceS.getUserByUsername(testUsernameHost);

        assertTrue(result);
        assertNull(userNHServer.getId());
        assertNotNull(room.getId());
        assertEquals(room.getPlayers(), new ArrayList<User>(){{add(userHostS);}});
    }

    @Test
    void testGetRoomId(){
        String testUsernameHost = "testH";
        String testUsernameNonHost = "testNH";
        User userHost = serviceS.createUser(testUsernameHost);
        User user = serviceS.createUser(testUsernameNonHost);
        Room room = this.serviceS.createRoom(userHost);
        boolean result = this.serviceS.joinRoom(userHost, room.getId());
        result = this.serviceS.joinRoom(user, room.getId());

        Room room1 = this.serviceS.getRoomById(room.getId());

        assertEquals(room, room1);
    }

    @Test
    void testMatchStarted() throws InterruptedException, URISyntaxException {
        String testUsernameHost = "aaa";
        ///host
        User userH = this.serviceS.createUser(testUsernameHost);
        SocketIOClientTest clientH = new SocketIOClientTest(testingHost, testingPort);
        clientH.connect(testUsernameHost);
        Thread.sleep(1000); 
        Room room = this.serviceS.createRoom(userH);
        boolean result = this.serviceS.joinRoom(userH, room.getId());
        var clientIOServerH = this.serviceS.getClientByUsername(testUsernameHost);

        ///non host
        String testUsernameNonHost = "bbb";
        User userNH = this.serviceS.createUser(testUsernameNonHost);
        SocketIOClientTest clientNH = new SocketIOClientTest(testingHost, testingPort);
        clientNH.connect(testUsernameNonHost);
        Thread.sleep(1000); 
        result = this.serviceS.joinRoom(userNH, room.getId());
        var clientIOServerNH = this.serviceS.getClientByUsername(testUsernameNonHost);

        var orbTest = new ObservableTest();
        this.serviceS.addObserver(orbTest);

        result = this.serviceS.addRoomSettings(room.getId(), new RoomSettings(1,1,1));

        assertTrue(result);
        assertEquals(orbTest.lastEvent.getEventType(), ObserverEventTypes.MATCH_STARTED);
        assertEquals((String)orbTest.lastEvent.getBody(), room.getId());
    }

    @Test
    void testTurnEnded() throws InterruptedException, URISyntaxException {
        var word_test = new Word ((long)1,"test");
        String testUsernameHost = "aaa";
        ///host
        User userH = this.serviceS.createUser(testUsernameHost);
        SocketIOClientTest clientH = new SocketIOClientTest(testingHost, testingPort);
        clientH.connect(testUsernameHost);
        Thread.sleep(1000); 
        Room room = this.serviceS.createRoom(userH);
        boolean result = this.serviceS.joinRoom(userH, room.getId());
        var clientIOServerH = this.serviceS.getClientByUsername(testUsernameHost);

        ///non host
        String testUsernameNonHost = "bbb";
        User userNH = this.serviceS.createUser(testUsernameNonHost);
        SocketIOClientTest clientNH = new SocketIOClientTest(testingHost, testingPort);
        clientNH.connect(testUsernameNonHost);
        Thread.sleep(1000); 
        result = this.serviceS.joinRoom(userNH, room.getId());
        var clientIOServerNH = this.serviceS.getClientByUsername(testUsernameNonHost);

        var orbTest = new ObservableTest();
        this.serviceS.addObserver(orbTest);
        result = this.serviceS.addRoomSettings(room.getId(), new RoomSettings(1,3,1));

        ChooseWordEvent event = new ChooseWordEvent(new ChooseWordEventBody(userH, word_test));
        String eventJson = "" ;
        try {
            eventJson = new ObjectMapper().writeValueAsString(event.getBody());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(eventJson);

        clientH.clientSocket.emit(new ChooseWordEvent().getName(), eventJson);
        Thread.sleep(1000);

        room = this.serviceS.getRoomById(room.getId());

        assertEquals(room.getStatus(), RoomStatus.InTurn);
        Thread.sleep(3000);

        room = this.serviceS.getRoomById(room.getId());
        assertEquals(room.getStatus(), RoomStatus.Started);
        assertEquals(orbTest.lastEvent.getEventType(), ObserverEventTypes.TIMER_ENDED);
        assertEquals((String)orbTest.lastEvent.getBody(), room.getId());
    }
}
