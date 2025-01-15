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
        SocketIOClient client = new SocketIOClient(testingHost, testingPort);
        client.connect(testUsername);
    
        Thread.sleep(1000); 
        assertTrue(client.isConnected(), "Client failed to connect.");
    }

    @Test
    void testConnectionLinkHost() throws InterruptedException, URISyntaxException {
        String testUsername = "aaa";
        User user = this.serviceS.createUser(testUsername);
        SocketIOClient client = new SocketIOClient(testingHost, testingPort);
        client.connect(testUsername);
        Thread.sleep(1000); 

        user = this.serviceS.getUserByUsername(testUsername);
        
        assertTrue(client.isConnected(), "Client failed to connect.");
        assertNotNull(user.getId());
    }

    @Test
    void testConnectionLinkNonHost() throws InterruptedException, URISyntaxException {
        String testUsername = "aaa";
        SocketIOClient client = new SocketIOClient(testingHost, testingPort);
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

        SocketIOClient client = new SocketIOClient(testingHost, testingPort);
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
        SocketIOClient clientH = new SocketIOClient(testingHost, testingPort);
        clientH.connect(testUsernameHost);
        Thread.sleep(1000); 
        Room room = this.serviceS.createRoom(userH);
        boolean result = this.serviceS.joinRoom(userH, room.getId());

        ///non host
        String testUsernameNonHost = "bbb";
        User userNH = this.serviceS.createUser(testUsernameNonHost);
        SocketIOClient clientNH = new SocketIOClient(testingHost, testingPort);
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
    void testConnectionJoinHostRoomSignal() throws InterruptedException, URISyntaxException {
        String testUsername = "aaa";
        User userH = this.serviceS.createUser(testUsername);
        Room room = this.serviceS.createRoom(userH);

        SocketIOClient client = new SocketIOClient(testingHost, testingPort);
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
        assertNotNull(client.room);
    }

    // @Test
    // void testLeaveClient() throws InterruptedException, URISyntaxException {
    //     String testUsername = "aaa";
    //     User userH = this.serviceS.createUser(testUsername);
    //     Room room = this.serviceS.createRoom(userH);

    //     SocketIOClient client = new SocketIOClient(testingHost, testingPort);
    //     client.connect(testUsername);
    //     Thread.sleep(1000); 

    //     boolean result = this.serviceS.joinRoom(userH, room.getId());
        
    //     User userHostS = this.serviceS.getUserByUsername(testUsername);
        
    //     assertTrue(client.isConnected(), "Client failed to connect.");
    //     assertTrue(userHostS.getIsHost());
    //     assertTrue(result);
    // }

    

    // @Test
    // void testSocketIoEventHandling() throws URISyntaxException, InterruptedException {
    //     // Connect to the server using a socket.io client
    //     Socket client = IO.socket("http://" + testingHost + ":" + testingPort);

    //     CountDownLatch latch = new CountDownLatch(1);
    //     final StringBuilder response = new StringBuilder();

    //     // Listen for the server's response
    //     client.on("testResponse", new Emitter.Listener() {
    //         @Override
    //         public void call(Object... args) {
    //             response.append((String) args[0]);
    //             latch.countDown();
    //         }
    //     });

    //     // Connect to the server
    //     client.connect();
    //     assertTrue(client.connected());

    //     // Emit an event to the server
    //     client.emit("testEvent", "World");

    //     // Wait for the server response
    //     boolean success = latch.await(5, TimeUnit.SECONDS);
    //     assertTrue(success, "The server did not respond in time");
    //     assertEquals("Hello World", response.toString());

    //     client.disconnect();
    // }
}
