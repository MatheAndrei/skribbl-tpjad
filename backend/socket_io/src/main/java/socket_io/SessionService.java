package socket_io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.corundumstudio.socketio.SocketIOClient;

import domain.DrawnImage;
import domain.Message;
import domain.Room;
import domain.User;
import domain.Word;
import domain.enums.RoomStatus;

public class SessionService {
    private Map<User, SocketIOClient> users;
    private List<User> pendingUsers;
    private List<Room> rooms;
    

    public SessionService(){
        this.pendingUsers = new ArrayList<>();
        this.users = new HashMap<>();
        this.rooms = new ArrayList<>();
    }

    protected String generateRoomCode(int length) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }


    public User createUser(String username){
        User user =  new User();
        /// should be move
        user.setUsername(username);
        user.setIsDrawer(null);
        user.setIsHost(null);
        user.setHasGuessed(null);

        pendingUsers.add(user);
        return user;
    }

    public boolean linkClient(SocketIOClient client, String username){
        User unlinkedUser = null; 
        for (var user : pendingUsers){
            if (user.getUsername().equals(username)){
                unlinkedUser = user;
                break;
            }
        }
        if (unlinkedUser != null){
            unlinkedUser.setId(client.getSessionId().toString());
            users.put(unlinkedUser, client);
        }
        return unlinkedUser != null;
    }

    public Room createRoom(User host){
        String roomId = this.generateRoomCode(6);
        Room room = new Room();
        /// should be moved
        host.setIsHost(true);
        room.setHost(host);
        room.setId(roomId);
        room.setStatus(RoomStatus.Undefined);
        room.setPlayers(new ArrayList<>());

        rooms.add(room);
        return room;
    }

    public boolean joinRoom(User user, String roomId){
        Room room = this.getRoomById(roomId);
        ///shoudl be moved
        if(room.getHost().equals(user)){
            room.setStatus(RoomStatus.Waiting);
        }
        return room.getPlayers().add(user);
    }

    private boolean deleteRoom(Room room){
        return rooms.remove(room);
    }

    public boolean startGame(Room room){
        return true;
    }

    private boolean removeUserFromRoom(User user){
        boolean isDeleted = false;
        Room room = this.getClientRoom(user);
        if (room.getId() == null ) return false;
        if(room.getHost().equals(user)){
            // change host here
        }
        if(room.getPlayers().contains(user)){
            room.getPlayers().remove(user);
            isDeleted = true;
        }
        if (room.getPlayers().isEmpty()){
            this.deleteRoom(room);
        }
        return isDeleted;
    }

    public boolean removeClient(SocketIOClient client){
        User userToDelete = null;
        for (var entry : users.entrySet()){
            if(entry.getValue().equals(client)){
                userToDelete = entry.getKey();
            }
        }
        return userToDelete != null && this.removeUser(userToDelete);
    }

    public boolean removeUser(User user){
        return (pendingUsers.remove(user) || this.users.remove(user) != null) && this.removeUserFromRoom(user);
    }

    public List<SocketIOClient> getSocketIOClientsForRoom(Room room){
        var socketIoClients = new ArrayList<SocketIOClient>();
        for(var user : room.getPlayers()){
            socketIoClients.add(users.get(user));
        }
        return socketIoClients;
    }

    public Room getClientRoom(User user){
        Room roomToSend = new Room();
        for (var room : rooms){
            if(room.getPlayers().contains(user)){
                roomToSend  = room;
                break;
            }
        }
        return roomToSend;
    }

    public Room getRoomById(String id){
        Room roomToSend = new Room();
        for (var room : rooms){
            if(room.getId().equals(id)){
                roomToSend  = room;
                break;
            }
        }
        return roomToSend;
    }

    public boolean addMessage(Message message){
        return true;
    }

    public boolean addChosenWord(User user, Word word){
        return true;
    }

    public boolean updateDrawnImage(User user, DrawnImage image){
        return true;
    }
}
