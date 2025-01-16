package springBoot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import domain.*;
import org.springframework.stereotype.Service;

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
    private HashMap<String, List<User>> correctUsers;
    private final int scoreTurn = 150;
    
    @PostConstruct
    private void initService(){
        this.users = new HashMap<>();
        this.rooms = new ArrayList<>();
        this.subscribers = new ArrayList<>();
        this.correctUsers = new HashMap<>();
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
//        if (users.keySet().isEmpty() || !users.containsKey(username)) {
            User user = new User();
            user.setUsername(username);
            user.setIsDrawer(null);
            user.setIsHost(null);
            user.setHasGuessed(null);
            this.saveUser(user);
            return user;
//        }
//        return null;
    }

    public void saveUser(User user){
        users.put(user.getUsername(), user);
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
        if(room.getPlayers().contains(user)){
            room.getPlayers().remove(user);
            isDeleted = true;
        }
        if (room.getPlayers().isEmpty()){
            this.deleteRoom(room);
        }else if(room.getHost().equals(user)){
            room.getPlayers().get(0).setIsHost(true);
            room.setHost(room.getPlayers().get(0));
        }
        if (room.getMatch() != null){
            /// remove turns with the removed user
            List<Turn> turnsToRemove = new ArrayList<>();
            for(var turn: room.getMatch().getCurrentRound().getTurns()){
                if (turn.getDrawerUser().equals(user)) {
                    turnsToRemove.add(turn);
                }
            }
            for(var t : turnsToRemove){
                room.getMatch().getCurrentRound().getTurns().remove(t);
            }
        }
        return isDeleted;
    }

    public boolean removeUser(User user){
        return this.users.remove(user.getUsername()) != null && this.removeUserFromRoom(user);
    }

    public Room hostRoom(String username){
        User userH = this.createUser(username);
        if (userH != null){
            Room room = this.createRoom(userH);
            return room;
        }
        return new Room();
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
        if (user == null || user.getId() == null) return false;
        Room room = this.getRoomById(roomId);
        if (room.getId() == null) {
            return false;
        }
        if(room.getHost().getUsername().equals(user.getUsername())){
            room.setStatus(RoomStatus.Waiting);
            var host = this.getUserByUsername(user.getUsername());
            host.setIsHost(true);
            this.saveUser(host);
            room.setHost(host); /// for id update and status
        }
        else{
            var nonHost = this.getUserByUsername(user.getUsername());
            nonHost.setIsHost(false);
            this.saveUser(nonHost);
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
        Round round = createNewRound(room);
        Match match = new Match(round, new ArrayList<>());
        room.setMatch(match);
        for( var player: room.getPlayers()){
            if (room.getMatch().getCurrentRound().getCurrentTurn().getDrawerUser().equals(player)){
                player.setIsDrawer(true);
            }
            else{
                player.setIsDrawer(false);
            }
        }
        room.setStatus(RoomStatus.Started);
        return true;
    }

    private Round createNewRound(Room room){
        List<Turn> turns = new ArrayList<>();
        for(var player: room.getPlayers()){
            turns.add(new Turn(player, null,null));
        }
        Round round = new Round(null, turns);
        round.nextTurn();
        return round;
    }

    public boolean addMessage(Message message){
        User user = message.getSender();
        Room userRoom = this.getUserRoom(user);
        Word chosenWord = userRoom.getMatch().getCurrentRound().getCurrentTurn().getCurrentWord();
        checkMessageIsWord(message, chosenWord);
        if(checkMessageIsWord(message,chosenWord)){
            userRoom.getMatch().getChat().add(new Message(user, "User "+ user.getUsername()+" guessed the word"));
            User userSaved = this.getUserByUsername(user.getUsername());
            userSaved.setHasGuessed(true);
            if(!correctUsers.containsKey(userRoom.getId())){
                correctUsers.put(userRoom.getId(), new ArrayList<>());
            }
            if (!correctUsers.get(userRoom.getId()).contains(userSaved))
                correctUsers.get(userRoom.getId()).add(userSaved);
            if(correctUsers.get(userRoom.getId()).size()== userRoom.getPlayers().size()-1){
                userRoom.setStatus(RoomStatus.Started);
                this.timeUpForRoom(userRoom);
            }

        }else{
            userRoom.getMatch().getChat().add(message);
        }
        return true;
    }

    public boolean checkMessageIsWord(Message message, Word word ){
        if(message.getMessage().equals(word.getWord())){
            return true;
        }
        return false;
    }

    public boolean addChosenWord(User user, Word word){

        /// round start
        var room = this.getUserRoom(user);
        this.notifyObservers(new ObserverEvent(ObserverEventTypes.TIMER_STARTED, room.getId()));
        room.getMatch().getCurrentRound().getCurrentTurn().getDrawerUser().setIsDrawer(true);
        room.getMatch().getCurrentRound().getCurrentTurn().setCurrentWord(word);
        room.setTimer(room.getSettings().getTimePerTurn());
        room.setStatus(RoomStatus.InTurn);
        return true;
    }

    public boolean updateDrawnImage(User user, DrawnImage image){
        Room room = getUserRoom(user);
        if (room.getMatch() != null && room.getMatch().getCurrentRound() != null && room.getMatch().getCurrentRound().getCurrentTurn() != null) {
            room.getMatch().getCurrentRound().getCurrentTurn().setImage(image);
        }
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
                        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                        if (room.getTimer() <= 0) {
                            this.timeUpForRoom(room);
                            room.setStatus(RoomStatus.Started);
                            System.out.println("a");
                        }
                        System.out.println("b");
                    }
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void calculateScores(User userDrawer,List<User> correctUsersPerRoom){
        System.out.println("PISATU:");
        System.out.println(correctUsersPerRoom);
        if (correctUsersPerRoom != null) {
            for(User user: correctUsersPerRoom){
                System.out.println(user);
                user.setScore(user.getScore()+scoreTurn-correctUsersPerRoom.indexOf(user) * 50);
            }
            if (userDrawer != null){
                System.out.println("!!!!!!!   PULAAAA   !!!!!!!");
                userDrawer.setScore(userDrawer.getScore() + 50 * correctUsersPerRoom.size());
            }

            correctUsersPerRoom.clear();
        }
    }



    private void timeUpForRoom(Room room){
        System.out.println("1");
        calculateScores(room.getMatch().getCurrentRound().getCurrentTurn().getDrawerUser(), correctUsers.get(room.getId()));
        System.out.println("2");
        this.notifyObservers(new ObserverEvent(ObserverEventTypes.TIMER_ENDED, room.getId()));
        System.out.println("3");
        room.getMatch().getCurrentRound().nextTurn();
        if(room.getMatch().getCurrentRound().getCurrentTurn() == null){
            if(room.getMatch().getCurrentRoundNum()>=room.getSettings().getNumRounds()-1){
                this.notifyObservers(new ObserverEvent(ObserverEventTypes.MATCH_ENDED, room.getId()));
            }else{
                Round round = createNewRound(room);
                room.getMatch().setCurrentRound(round);
                room.getMatch().incrementCurrentRoundNum();
            }
        }
        if(room.getMatch().getCurrentRound().getCurrentTurn() != null){
            for( var player: room.getPlayers()){
                if (room.getMatch().getCurrentRound().getCurrentTurn().getDrawerUser().equals(player)){
                    player.setIsDrawer(true);
                }
                else{
                    player.setIsDrawer(false);
                }
            }
        }
    }
}
