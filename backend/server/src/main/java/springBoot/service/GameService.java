package springBoot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import domain.DrawnImage;
import domain.Message;
import domain.Room;
import domain.RoomSettings;
import domain.User;
import domain.Word;
import domain.enums.RoomStatus;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import springBoot.socket_io.observer.IObservable;
import springBoot.socket_io.observer.IObserver;
import springBoot.socket_io.observer.ObserverEvent;
import springBoot.socket_io.observer.ObserverEventTypes;

@Service
@NoArgsConstructor
public class GameService implements IObserver{
    
    private Map<String, User> users; 
    private List<Room> rooms;
    private List<IObservable> subscribers;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    
    @PostConstruct
    private void initService(){
        this.users = new HashMap<>();
        this.rooms = new ArrayList<>();
        this.subscribers = new ArrayList<>();
        System.out.println("Game Service " + this);
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
        user.setUsername(username);
        user.setIsDrawer(null);
        user.setIsHost(null);
        user.setHasGuessed(null);
        users.put(username, user);
        return user;
    }

    public User getUserByUsername(String username){
        for (var userN : users.keySet()){
            if (userN.equals(username)){
                return users.get(userN);
            }
        }
        return new User();
    }

    public List<User> getUsers(){
        var ls = new ArrayList<User>();
        ls.addAll(users.values());
        return ls;
    }

    public boolean updateExistingUser(User user){
        var userSaved = this.getUserByUsername(user.getUsername());
        users.put(userSaved.getUsername(), user); 
        return true;
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

    private boolean removeUserFromRoom(User user){
        boolean isDeleted = false;
        Room room = this.getUserRoom(user);
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

    public boolean removeUser(User user){
        return this.users.remove(user.getUsername()) != null && this.removeUserFromRoom(user);
    }

    public Room hostRoom(String username){
        User userH = this.createUser(username);
        Room room = this.createRoom(userH);
        return room;
    }

    public boolean addRoomSettings(String roomId, RoomSettings settings){
        var room = this.getRoomById(roomId);
        if (room.getId() != null)
        {
            room.setSettings(settings);
            this.startGame(room);
            this.notifyObservers(new ObserverEvent(ObserverEventTypes.MATCH_STARTED, room.getId()));
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
    
    public Room getUserRoom(User user){
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

    private boolean deleteRoom(Room room){
        return rooms.remove(room);
    }


    public boolean startGame(Room room){
        /// TODO
        return true;
    }

    public boolean addMessage(Message message){
        /// TODO
        return true;
    }

    public boolean addChosenWord(User user, Word word){
        /// TODO
        /// round start
        var room = this.getUserRoom(user);
        this.notifyObservers(new ObserverEvent(ObserverEventTypes.TIMER_STARTED, room.getId()));
        room.setStatus(RoomStatus.InTurn);
        room.setTimer(room.getSettings().getTimePerTurn());
        return true;
    }

    public boolean updateDrawnImage(User user, DrawnImage image){
        /// TODO
        return true;
    }

    public void reset(){
        this.users = new HashMap<>();
        this.rooms = new ArrayList<>();
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
        System.out.println("Thread timer started");
        executor.scheduleAtFixedRate(() -> {
            synchronized (rooms) {
                for (Room room : rooms) {
                    if(room.getStatus() == RoomStatus.InTurn){
                        room.decrementTimer();
                        if (room.getTimer() <= 0) {
                            this.timeUpForRoom(room);
                            room.setStatus(RoomStatus.Started);
                        }
                    }
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
    // moved
    private void timeUpForRoom(Room room){
        /// TODO round end
        this.notifyObservers(new ObserverEvent(ObserverEventTypes.TIMER_ENDED, room.getId()));
        /// TODO check if match end
        this.notifyObservers(new ObserverEvent(ObserverEventTypes.TIMER_ENDED, room.getId()));
    }
}
