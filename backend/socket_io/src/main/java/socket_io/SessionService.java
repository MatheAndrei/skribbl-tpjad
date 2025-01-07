package socket_io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.corundumstudio.socketio.SocketIOClient;

import domain.DrawnImage;
import domain.Message;
import domain.Room;
import domain.User;
import domain.Word;

public class SessionService {
    private Map<SocketIOClient, User> users;
    private List<Room> rooms;

    public SessionService(){
        this.users = new HashMap<>();
        this.rooms = new ArrayList<>();
    }

    public boolean removeClient(SocketIOClient client){
        return true;
    }

    public boolean removeClient(User client){
        return true;
    }

    public List<SocketIOClient> getSocketIOClientsForRoom(Room room){
        return new ArrayList<>();
    }

    public Room getClientRoom(User user){
        return new Room();
    }

    public Room getRoomById(String id){
        return new Room();
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
