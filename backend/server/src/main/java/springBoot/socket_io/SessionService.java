package springBoot.socket_io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.corundumstudio.socketio.SocketIOClient;

import domain.DrawnImage;
import domain.Message;
import domain.Room;
import domain.RoomSettings;
import domain.User;
import domain.Word;
import domain.enums.RoomStatus;
import springBoot.socket_io.observer.IObservable;
import springBoot.socket_io.observer.IObserver;
import springBoot.socket_io.observer.ObserverEvent;
import springBoot.socket_io.observer.ObserverEventTypes;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class SessionService implements IObserver{
    private Map<User, SocketIOClient> users;
    private List<User> pendingUsers;
    private List<Room> rooms;
    private List<IObservable> subscribers;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    
    public SessionService(){
        this.pendingUsers = new ArrayList<>();
        this.users = new HashMap<>();
        this.rooms = new ArrayList<>();
        this.subscribers = new ArrayList<>();
        startTimerThread();
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

    public User getUserByUsername(String username){
        for (var user : users.keySet()){
            if (user.getUsername().equals(username)){
                return user;
            }
        }
        return new User();
    }

    public List<User> getUsers(){
        var ls = new ArrayList<User>();
        ls.addAll(users.keySet());
        return ls;
    }

    public List<User> getPendingUsers(){
        return pendingUsers;
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
            pendingUsers.remove(unlinkedUser);
        }
        return unlinkedUser != null;
    }

    public Room createRoom(User host){
        Room room = new Room();
        String roomId = this.generateRoomCode(6);
        /// should be moved
        room.setHost(host);
        room.setId(roomId);
        room.setStatus(RoomStatus.Undefined);
        room.setPlayers(new ArrayList<>());
        rooms.add(room);
        
        return room;
    }

    public Room hostRoom(String username){
        User userH = this.createUser(username);
        Room room = this.createRoom(userH);
        return room;
    }

    // private boolean updateExistingUser(User user){
    //     var userSaved = this.getUserByUsername(user.getUsername());
    //     var userSavedOld =  userSaved;

    //     users.put(userSaved, users.get(userSavedOld)); 
    //     return true;
    // }

    public boolean addRoomSettings(String roomId, RoomSettings settings){
        var room = this.getRoomById(roomId);
        if (room.getId() != null)
        {
            room.setSettings(settings);
            this.startGame(room);
            return true;
        }
        return false;
    }

    public boolean joinRoom(User user, String roomId){
        if (user.getId() == null) return false;
        Room room = this.getRoomById(roomId);
        ///shoudl be moved
        if(room.getHost().getUsername().equals(user.getUsername())){
            room.setStatus(RoomStatus.Waiting);
            var host = this.getUserByUsername(user.getUsername());
            host.setIsHost(true);
            room.setHost(host); /// for id update and status
        }
        else{
            var nonHost = this.getUserByUsername(user.getUsername());
            nonHost.setIsHost(false);
        }
        return room.getPlayers().add(user);
    }

    private boolean deleteRoom(Room room){
        return rooms.remove(room);
    }

    public boolean startGame(Room room){
        /// TODO
        this.notifyObservers(new ObserverEvent(ObserverEventTypes.MATCH_STARTED, room.getId()));
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

    public void reset(){
        this.pendingUsers = new ArrayList<>();
        this.users = new HashMap<>();
        this.rooms = new ArrayList<>();
    }

    public boolean addMessage(Message message){
        /// TODO
        return true;
    }

    public boolean addChosenWord(User user, Word word){
        /// TODO
        /// round start
        var room = this.getClientRoom(user);
        room.setStatus(RoomStatus.InTurn);
        return true;
    }

    public boolean updateDrawnImage(User user, DrawnImage image){
        /// TODO
        return true;
    }

	@Override
	public boolean addObserver(IObservable obs) {
		return this.subscribers.add(obs);
	}

	@Override
	public void notifyObservers(ObserverEvent event) {
		for(var obs : this.subscribers){
            obs.update(event);
        }
	}

    private void startTimerThread() {
        executor.scheduleAtFixedRate(() -> {
            synchronized (rooms) {
                for (Room room : rooms) {
                    if(room.getStatus() == RoomStatus.InTurn){
                        room.decrementTimer();
                        room.setStatus(RoomStatus.Started);
                        if (room.getTimer() <= 0) {
                            this.timeUpForRoom(room);
                        }
                    }
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
    private void timeUpForRoom(Room room){
        /// TODO round end
        this.notifyObservers(new ObserverEvent(ObserverEventTypes.TIMER_ENDED, room.getId()));
    }
}
