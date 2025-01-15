package springBoot.socket_io;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.*;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import domain.DrawnImage;
import domain.Message;
import domain.Room;
import domain.User;
import domain.Word;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.*;

import springBoot.socket_io.events.BasicEvent;
import springBoot.socket_io.events.client.TimerEndedEvent;
import springBoot.socket_io.events.client.UpdateAllEvent;
import springBoot.socket_io.events.client.body.UpdateAllEventBody;
import springBoot.socket_io.events.server.ChatMessageEvent;
import springBoot.socket_io.events.server.body.ChatMessageEventBody;
import springBoot.socket_io.events.server.ChooseWordEvent;
import springBoot.socket_io.events.server.body.ChooseWordEventBody;
import springBoot.socket_io.events.server.DisconnectEvent;
import springBoot.socket_io.events.server.body.DisconnectEventBody;
import springBoot.socket_io.events.server.DrawEvent;
import springBoot.socket_io.events.server.body.DrawEventBody;
import springBoot.socket_io.events.server.JoinRoomEvent;
import springBoot.socket_io.events.server.body.JoinRoomEventBody;
import springBoot.socket_io.observer.IObservable;
import springBoot.socket_io.observer.IObserver;
import springBoot.socket_io.observer.ObserverEvent;
import springBoot.socket_io.observer.ObserverEventTypes;

//@Component
@Slf4j
@Service
@RequiredArgsConstructor
public class SocketIOService implements IObservable{
    private final SocketIOServer server;
    private final SessionService sessionService;

    // @Autowired
    // public SocketIOService(SocketIOServer server) {
    //     this.server = server;
    //     this.sessionService = new SessionService();
    //     this.initializeServer();
    // }
    
    // public SocketIOService(SocketIOServer server, SessionService service) {
    //     this.server = server;
    //     this.sessionService = service;
    //     this.initializeServer();
    // }

    @PostConstruct
    private void initializeServer(){
        this.sessionService.addObserver(this);
        this.server.addDisconnectListener(this.onDisconnect());
        this.server.addConnectListener(this.onConnect());
        this.server.addEventListener((new DisconnectEvent()).getName(), DisconnectEventBody.class, this.onDisconnectClient());
        this.server.addEventListener((new ChatMessageEvent()).getName(), ChatMessageEventBody.class, this.onChatMesage());
        this.server.addEventListener((new ChooseWordEvent()).getName(), ChooseWordEventBody.class, this.onChooseWord());
        this.server.addEventListener((new DrawEvent()).getName(), DrawEventBody.class, this.onDraw());
        this.server.addEventListener((new JoinRoomEvent()).getName(), JoinRoomEventBody.class, this.onJoinRoom());
    
        /// for testing
        this.server.addEventListener("room_id", String.class, this.onGetRoomDataClient());
        this.server.addEventListener("room_host", String.class, this.onHostClientRoom());

    }

    private DataListener onGetRoomDataClient(){
        return (client, data, ackSender) -> {
            String roomId =  data.toString();
            System.out.println("Room with id: " + roomId);

            Room room = this.sessionService.getRoomById(roomId);
            client.sendEvent((new UpdateAllEvent()).getName(), new UpdateAllEvent(new UpdateAllEventBody(room)));
            // this.sentUpdateAllEvent(new UpdateAllEvent(new UpdateAllEvent().new UpdateAllEventBody(room)));
        };
    }

    private DataListener onHostClientRoom(){
        return (client, data, ackSender) -> {
            String username =  data.toString();
            System.out.println("Username for hosting : " + username);

            Room room = this.sessionService.hostRoom(username);
            client.sendEvent((new UpdateAllEvent()).getName(), new UpdateAllEvent(new UpdateAllEventBody(room)));
            // this.sentUpdateAllEvent(new UpdateAllEvent(new UpdateAllEvent().new UpdateAllEventBody(room)));
        };
    }


    private DisconnectListener onDisconnect(){
        return client -> {
            // var params = client.getHandshakeData().getUrlParams();
            // String room = params.get("room").stream().collect(Collectors.joining());
            // String username = params.get("username").stream().collect(Collectors.joining());
            User user = this.sessionService.getUserForClient(client);
            Room room =  this.sessionService.getClientRoom(user);
            client.leaveRoom(room.getId());
            this.sessionService.removeClient(client);

            this.sendEventToRoom(room.getId(), new UpdateAllEvent(new UpdateAllEventBody(room)));
        };
    }

    private ConnectListener onConnect(){
        return client -> {
            var handshakeData = client.getHandshakeData();
            String username = handshakeData.getSingleUrlParam("username");
            
            if (username != null && !username.isEmpty()) {
                System.out.println("Client connected with username: " + username);
                boolean result = this.sessionService.linkClient(client, username);
                System.out.println("Client is linked: " + result);
                if (!result){
                    User user = this.sessionService.createUser(username);
                    this.sessionService.linkClient(client, username);
                }
            } else {
                System.out.println("Client connected without a username.");
            }
        };
    }

    private void sendEventToRoom(String roomId, BasicEvent event){
        System.out.println("Send updates to room: " + roomId);
        server.getRoomOperations(roomId).getClients().forEach(cl -> {
            if (!event.getExcludedClients().contains(cl)){
                String eventJson = "" ;
                try {
                    eventJson = new ObjectMapper().writeValueAsString(event.getBody());
                } catch (JsonProcessingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                cl.sendEvent(event.getName(), eventJson);
            }
        });
    }

    private DataListener onDisconnectClient(){
        return (client, data, ackSender) -> {
            User sender = ((DisconnectEventBody)data).getSender();
            String roomId = this.sessionService.getClientRoom(sender).getId();
            client.leaveRoom(roomId);
            this.sessionService.removeUser(sender);

            Room room = this.sessionService.getRoomById(roomId);
            this.sendEventToRoom(room.getId(), new UpdateAllEvent(new UpdateAllEventBody(room)));
        };
    }

    private DataListener onChatMesage(){
        return (client, data, ackSender) -> {
            Message message = ((ChatMessageEventBody)data).getMessage();
            this.sessionService.addMessage(message);

            Room room = this.sessionService.getClientRoom(message.getSender());
            this.sendEventToRoom(room.getId(), new UpdateAllEvent(new UpdateAllEventBody(room)));
        };
    }

    private DataListener onChooseWord(){
        return (client, data, ackSender) -> {
            User sender = ((ChooseWordEventBody)data).getSender();
            Word word = ((ChooseWordEventBody)data).getWord();
            this.sessionService.addChosenWord(sender, word);

            Room room = this.sessionService.getClientRoom(sender);
            this.sendEventToRoom(room.getId(), new UpdateAllEvent(new UpdateAllEventBody(room)));
        };
    }

    private DataListener onDraw(){
        return (client, data, ackSender) -> {
            User sender = ((DrawEventBody)data).getSender();
            DrawnImage image = ((DrawEventBody)data).getImage();
            this.sessionService.updateDrawnImage(sender, image);

            Room room = this.sessionService.getClientRoom(sender);
            UpdateAllEvent event = new UpdateAllEvent(new UpdateAllEventBody(room));
            event.addExcludedClients(client); // exlude the client that is drawing so that he con continue to draw in peace
            this.sendEventToRoom(room.getId(), event); 
        };
    }

    private DataListener onJoinRoom(){
        return (client, data, ackSender) -> {
            User sender = ((JoinRoomEventBody)data).getSender();
            String roomId = ((JoinRoomEventBody)data).getRoomId();
            boolean result = this.sessionService.joinRoom(sender, roomId);
            System.out.println("User " + sender.getUsername()+ " just joined the room " + roomId+ " " + result);

            Room room = this.sessionService.getClientRoom(sender);
            client.joinRoom(room.getId());
            this.sendEventToRoom(room.getId(), new UpdateAllEvent(new UpdateAllEventBody(room)));
        };
    }

    public void start() {
        this.server.start();
        System.out.println("Socket.IO server started on " + this.server.getConfiguration().getHostname() + ":" + this.server.getConfiguration().getPort());
    }

    private void waitForPortRelease(int port) {
        int retries = 10; // Max number of retries
        int delay = 500; // Delay between retries in milliseconds
    
        for (int i = 0; i < retries; i++) {
            if (!isPortInUse(port)) {
                System.out.println("Port " + port + " is now free.");
                return;
            }
    
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread was interrupted while waiting for port release", e);
            }
        }
    
        throw new RuntimeException("Port " + port + " is still in use after waiting.");
    }

    private boolean isPortInUse(int port) {
    try (ServerSocket socket = new ServerSocket(port)) {
        socket.setReuseAddress(true);
        return false; // Port is free
    } catch (IOException e) {
        return true; // Port is in use
    }
}

    public void stop() {
        if (this.server != null) {
            this.server.stop();
            System.out.println("Socket.IO server stopped.");
            this.waitForPortRelease(this.server.getConfiguration().getPort());
        }
    }

    @Override
    public void update(ObserverEvent event){
        String roomId;
        Room room;
        switch (event.getEventType()) {
            case ObserverEventTypes.MATCH_STARTED:
                roomId = (String)event.getBody();
                room = this.sessionService.getRoomById(roomId);
                this.sendEventToRoom(roomId, new UpdateAllEvent(new UpdateAllEventBody(room)));
                break;
            case ObserverEventTypes.TIMER_ENDED:
                roomId = (String)event.getBody();
                this.sendEventToRoom(roomId, new TimerEndedEvent());
                break;
            default:
                break;
        }
    }
}
