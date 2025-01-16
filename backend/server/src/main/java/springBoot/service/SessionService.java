package springBoot.service;

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
import lombok.RequiredArgsConstructor;
import springBoot.socket_io.observer.IObservable;
import springBoot.socket_io.observer.IObserver;
import springBoot.socket_io.observer.ObserverEvent;
import springBoot.socket_io.observer.ObserverEventTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService implements IObserver, IObservable{
    private Map<String, SocketIOClient> users;
    private List<User> pendingUsers;
    private List<IObservable> subscribers;
    
    private GameService serviceGame;

    // public SessionService(){
    //     this.pendingUsers = new ArrayList<>();
    //     this.users = new HashMap<>();
    //     this.subscribers = new ArrayList<>();
    //     this.serviceGame = new GameService();
    //     this.serviceGame.addObserver(this);
    // }

    @Autowired
    public SessionService(GameService serviceGame){
        this.pendingUsers = new ArrayList<>();
        this.users = new HashMap<>();
        this.subscribers = new ArrayList<>();
        this.serviceGame = serviceGame;
        this.serviceGame.addObserver(this);
    }

    public User createUser(String username){
        User user =  serviceGame.createUser(username);
        if (user != null)
            pendingUsers.add(user);
        return user;
    }

    public User getUserByUsername(String username){
        return this.serviceGame.getUserByUsername(username);
    }

    public SocketIOClient getClientByUsername(String username){
        for (var useN : users.keySet()){
            if (useN.equals(username)){
                return users.get(useN);
            }
        }
        return null;
    }

    public User getUserForClient(SocketIOClient client){
        for (var elem : users.entrySet()){
            if (elem.getValue().getSessionId().equals(client.getSessionId())){
                return this.serviceGame.getUserByUsername(elem.getKey());
            }
        }
        return new User();
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
            this.serviceGame.updateExistingUser(unlinkedUser);
            users.put(unlinkedUser.getUsername(), client);
            pendingUsers.remove(unlinkedUser);
        }
        return unlinkedUser != null;
    }

    public Room hostRoom(String username){
        return this.serviceGame.hostRoom(username);
    }

    public Room createRoom(User host){
        return this.serviceGame.createRoom(host);
    }


    public boolean addRoomSettings(String roomId, RoomSettings settings){
        return this.serviceGame.addRoomSettings(roomId, settings);
    }

    public boolean joinRoom(User user, String roomId){
        return this.serviceGame.joinRoom(user, roomId);
    }
    
    public boolean startGame(Room room){
        room.setTimer(room.getSettings().getTimePerTurn());
        this.notifyObservers(new ObserverEvent(ObserverEventTypes.MATCH_STARTED, room.getId()));
        return this.startGame(room);
    }

    public boolean removeClient(SocketIOClient client){
        User userToDelete = null;
        for (var entry : users.entrySet()){
            if(entry.getValue().equals(client)){
                userToDelete = this.serviceGame.getUserByUsername(entry.getKey());
            }
        }
        return userToDelete != null && this.removeUser(userToDelete);
    }

    public boolean removeUser(User user){
        return (pendingUsers.remove(user) || this.users.remove(user.getUsername()) != null) && this.serviceGame.removeUser(user);
    }

    public List<SocketIOClient> getSocketIOClientsForRoom(Room room){
        var socketIoClients = new ArrayList<SocketIOClient>();
        for(var user : room.getPlayers()){
            socketIoClients.add(users.get(user));
        }
        return socketIoClients;
    }

    public Room getUserRoom(User user){
        return this.serviceGame.getUserRoom(user);
    }

    public Room getRoomById(String id){
        return this.serviceGame.getRoomById(id);
    }

    public void reset(){
        this.pendingUsers = new ArrayList<>();
        this.users = new HashMap<>();
    }

    public boolean addMessage(Message message){
        return this.serviceGame.addMessage(message);
    }

    public boolean addChosenWord(User user, Word word){
        return this.serviceGame.addChosenWord(user, word);
    }

    public boolean updateDrawnImage(User user, DrawnImage image){
        return this.serviceGame.updateDrawnImage(user, image);
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

    @Override
    public void update(ObserverEvent event) {
        this.notifyObservers(event);
    }
}
