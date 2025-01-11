package socket_io;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.Room;
import domain.User;
import domain.enums.RoomStatus;
import springBoot.socket_io.SessionService;

public class TestSessionService {
    
    private SessionService sessionService;

    @BeforeEach
    void initSessionService(){
        this.sessionService = new SessionService();
    }

    @Test
    void testCreateUser(){
        String testUsername= "test";
        User user = sessionService.createUser(testUsername);

        assertNull(user.getId());
        assertEquals(user.getUsername(), testUsername);
        assertNull(user.hasGuessed());
        assertNull(user.isDrawer());
        assertNull(user.isIsHost());
    } 

    @Test
    void testCreateRoom(){
        String testUsername= "test";
        User user = sessionService.createUser(testUsername);
        Room room = this.sessionService.createRoom(user);

        assertNotNull(room.getId());
        assertEquals(room.getHost(), user);
        assertEquals(room.getStatus(),RoomStatus.Undefined);
        assertEquals(room.getPlayers().size(),0);
        assertTrue(room.getHost().isIsHost());
    } 

    @Test
    void testJoinRoomHost(){
        String testUsername= "test";
        User user = sessionService.createUser(testUsername);
        Room room = this.sessionService.createRoom(user);
        boolean result = this.sessionService.joinRoom(user, room.getId());

        assertTrue(result);
        assertEquals(room.getStatus(),RoomStatus.Waiting);
        assertEquals(room.getHost(), user);
        assertEquals(room.getPlayers(), new ArrayList<User>(){{add(user);}});
    } 

    @Test
    void testJoinRoomNonHost(){
        String testUsernameHost = "testH";
        String testUsernameNonHost = "testNH";
        User userHost = sessionService.createUser(testUsernameHost);
        User user = sessionService.createUser(testUsernameNonHost);
        Room room = this.sessionService.createRoom(userHost);
        boolean result = this.sessionService.joinRoom(userHost, room.getId());

        result = this.sessionService.joinRoom(user, room.getId());

        assertTrue(result);
        assertEquals(room.getStatus(),RoomStatus.Waiting);
        assertEquals(room.getHost(), userHost);
        assertEquals(room.getPlayers(), new ArrayList<User>(){{add(userHost);add(user);}});
    } 

    @Test
    void testGetClientsRoom(){
        String testUsernameHost = "testH";
        String testUsernameNonHost = "testNH";
        User userHost = sessionService.createUser(testUsernameHost);
        User user = sessionService.createUser(testUsernameNonHost);
        Room room = this.sessionService.createRoom(userHost);
        boolean result = this.sessionService.joinRoom(userHost, room.getId());
        result = this.sessionService.joinRoom(user, room.getId());

        Room room1 = this.sessionService.getClientRoom(user);

        assertEquals(room, room1);
    } 

    @Test
    void testGetRoomId(){
        String testUsernameHost = "testH";
        String testUsernameNonHost = "testNH";
        User userHost = sessionService.createUser(testUsernameHost);
        User user = sessionService.createUser(testUsernameNonHost);
        Room room = this.sessionService.createRoom(userHost);
        boolean result = this.sessionService.joinRoom(userHost, room.getId());
        result = this.sessionService.joinRoom(user, room.getId());

        Room room1 = this.sessionService.getRoomById(room.getId());

        assertEquals(room, room1);
    } 

    @Test
    void testRemoveUserNonExistent(){
        String testUsernameNonHost = "testNH";
        User user = sessionService.createUser(testUsernameNonHost);

        boolean result = this.sessionService.removeUser(user);

        assertFalse(result);
    }
    
    @Test
    void testRemoveUserExistent(){
        String testUsernameHost = "testH";
        String testUsernameNonHost = "testNH";
        User userHost = sessionService.createUser(testUsernameHost);
        User user = sessionService.createUser(testUsernameNonHost);
        Room room = this.sessionService.createRoom(userHost);
        boolean result = this.sessionService.joinRoom(userHost, room.getId());
        result = this.sessionService.joinRoom(user, room.getId());

        result = this.sessionService.removeUser(user);

        assertTrue(result);
        assertNull(this.sessionService.getClientRoom(user).getId());
        assertEquals(this.sessionService.getClientRoom(userHost).getPlayers(), new ArrayList<User>(){{add(userHost);}});
    } 

    @Test
    void testRemoveAllUsersExistent(){
        String testUsernameHost = "testH";
        String testUsernameNonHost = "testNH";
        User userHost = sessionService.createUser(testUsernameHost);
        User user = sessionService.createUser(testUsernameNonHost);
        Room room = this.sessionService.createRoom(userHost);
        boolean result = this.sessionService.joinRoom(userHost, room.getId());
        result = this.sessionService.joinRoom(user, room.getId());

        result = this.sessionService.removeUser(user);
        result = this.sessionService.removeUser(userHost);

        assertTrue(result);
        assertNull(this.sessionService.getClientRoom(user).getId());
        assertNull(this.sessionService.getRoomById(room.getId()).getId());
    } 
}
