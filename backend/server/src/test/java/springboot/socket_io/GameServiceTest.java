package springboot.socket_io;

import static org.junit.jupiter.api.Assertions.*;

import domain.*;
import domain.enums.RoomStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import springBoot.service.GameService;

@SpringBootTest(classes = GameService.class)
@ActiveProfiles("test")
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService.reset();
    }

    @Test
    void testCreateUser() {
        User user = gameService.createUser("JohnDoe");
        assertNotNull(user, "User should not be null");
        assertEquals("JohnDoe", user.getUsername(), "Username should match the input");
    }

    @Test
    void testCreateUserEmpty() {
        User user = gameService.createUser("");
        assertNotNull(user, "User should still be created");
        assertEquals("", user.getUsername(), "Username should be empty when input is empty");
    }

    @Test
    void testGetUserByUsername() {
        User createdUser = gameService.createUser("Alice");
        User fetchedUser = gameService.getUserByUsername("Alice");
        assertEquals(createdUser, fetchedUser, "Fetched user should match the created user");
    }

    @Test
    void testGetUserByUsernameFail() {
        User fetchedUser = gameService.getUserByUsername("NonExistentUser");
        assertNotNull(fetchedUser, "Should return a non-null user object even if user does not exist");
        assertNull(fetchedUser.getUsername(), "Username should be null for non-existent users");
    }

    @Test
    void testCreateRoomSettings() {
        User host = gameService.createUser("HostUser");
        Room room = gameService.createRoom(host);
        RoomSettings settings = new RoomSettings();
        settings.setNumRounds(5);

        boolean result = gameService.addRoomSettings(room.getId(), settings);
        assertTrue(result, "Settings should be added successfully");
        assertEquals(settings, room.getSettings(), "Room settings should match the input settings");
    }

    @Test
    void testAddRoomSettingsFail() {
        RoomSettings settings = new RoomSettings();
        boolean result = gameService.addRoomSettings("InvalidRoomId", settings);
        assertFalse(result, "Adding settings to a non-existent room should fail");
    }

    @Test
    void testCreateRoom() {
        User host = gameService.createUser("HostUser");
        Room room = gameService.createRoom(host);

        assertNotNull(room, "Room should not be null");
        assertEquals(host, room.getHost(), "Host of the room should be the user who created it");
        assertNotNull(room.getId(), "Room ID should not be null");
        assertEquals(6, room.getId().length(),"Room ID length should be 6");
        assertEquals(RoomStatus.Undefined, room.getStatus(), "Initial room status should be Undefined");
        assertTrue(gameService.getUsers().contains(host), "Host should be added to the list of users");
    }

    @Test
    void testCreateRoomFail() {
        User invalidHost = new User(); // User not created properly or without a valid ID
        Room room = gameService.createRoom(invalidHost);

        assertNotNull(room, "Room should still be created even if host is invalid");
        assertNull(room.getHost().getUsername(), "Room's host username should be null for invalid host");
        assertNotNull(room.getId(), "Room ID should still be generated");
        assertEquals(RoomStatus.Undefined, room.getStatus(), "Initial room status should be Undefined");
        assertFalse(gameService.getUsers().contains(invalidHost), "Invalid host should not be added to the list of users");
    }


    @Test
    void testJoinRoom() {
        User user = new User("1","player1");
        Room room = gameService.createRoom(user);

        boolean result = gameService.joinRoom(user, room.getId());
        assertTrue(result, "User should be able to join the room");
        assertTrue(room.getPlayers().contains(user), "Room should contain the user");
    }

    @Test
    void testJoinRoomFail() {
        User user = new User(); // User without a valid ID
        boolean result = gameService.joinRoom(user, "InvalidRoomId");
        assertFalse(result, "Joining a non-existent room should fail");
    }

    @Test
    void testRemoveUser() {
        User user = gameService.createUser("player1");
        user.setId("1");
        User userToRemove = gameService.createUser("player2");
        userToRemove.setId("2");
        Room room = gameService.createRoom(user);
        gameService.joinRoom(userToRemove, room.getId());

        boolean result = gameService.removeUser(userToRemove);
        assertTrue(result, "User should be removed successfully");
        assertFalse(room.getPlayers().contains(userToRemove), "Room should no longer contain the user");
    }

    @Test
    void testRemoveUserFail() {
        User user = new User(); // User not added to any room or user map
        boolean result = gameService.removeUser(user);
        assertFalse(result, "Removing a non-existent user should fail");
    }

    @Test
    void testAddAMessageWhichIsNotGuessedWord() {
        User user = gameService.createUser("Player1");
        RoomSettings settings = new RoomSettings(3,3,3);
        user.setId("1");
        Room room = gameService.createRoom(user);
        gameService.joinRoom(user, room.getId());
        gameService.addRoomSettings(room.getId(), settings);
        gameService.startGame(room);
        gameService.addChosenWord(user,new Word("TheWord"));

        Message message = new Message(user, "NotTheWord");
        boolean result = gameService.addMessage(message);
        assertTrue(result, "Message should be added successfully");
        assertTrue(room.getMatch().getChat().contains(message), "Room chat should contain the message");
    }

    @Test
    void testAddAMessageWhichIsTheGuessedWord() {
        User user = gameService.createUser("Player1");
        RoomSettings settings = new RoomSettings(3,3,3);
        user.setId("1");
        Room room = gameService.createRoom(user);
        gameService.joinRoom(user, room.getId());
        gameService.addRoomSettings(room.getId(), settings);
        gameService.startGame(room);
        gameService.addChosenWord(user,new Word("TheWord"));

        Message message = new Message(user, "TheWord");
        boolean result = gameService.addMessage(message);
        assertTrue(result, "Message should be added successfully");
        assertFalse(room.getMatch().getChat().contains(message), "Room chat should contain the message");
    }


}
