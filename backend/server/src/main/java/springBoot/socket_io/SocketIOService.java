package springBoot.socket_io;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.*;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

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
import springBoot.socket_io.events.client.UpdateAllEvent;
import springBoot.socket_io.events.server.ChatMessageEvent;
import springBoot.socket_io.events.server.ChooseWordEvent;
import springBoot.socket_io.events.server.DisconnectEvent;
import springBoot.socket_io.events.server.DrawEvent;

//@Component
@Slf4j
@Service
@RequiredArgsConstructor
public class SocketIOService {
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
        this.server.addDisconnectListener(this.onDisconnect());
        this.server.addConnectListener(this.onConnect());
        this.server.addEventListener((new DisconnectEvent()).getName(), DisconnectEvent.DisconnectEventBody.class, this.onDisconnectClient());
        this.server.addEventListener((new ChatMessageEvent()).getName(), ChatMessageEvent.ChatMessageEventBody.class, this.onChatMesage());
        this.server.addEventListener((new ChooseWordEvent()).getName(), ChooseWordEvent.ChooseWordEventBody.class, this.onChooseWord());
        this.server.addEventListener((new DrawEvent()).getName(), DrawEvent.DrawEventBody.class, this.onDraw());
    
        this.server.addEventListener("room_id", String.class, this.onGetRoomDataClient());

    }

    private DisconnectListener onDisconnect(){
        return client -> {
            // var params = client.getHandshakeData().getUrlParams();
            // String room = params.get("room").stream().collect(Collectors.joining());
            // String username = params.get("username").stream().collect(Collectors.joining());

            this.sessionService.removeClient(client);
        };
    }

    private ConnectListener onConnect(){
        return client -> {
            var handshakeData = client.getHandshakeData();
            String username = handshakeData.getSingleUrlParam("username");
            
            if (username != null && !username.isEmpty()) {
                System.out.println("Client connected with username: " + username);
            } else {
                System.out.println("Client connected without a username.");
            }
            boolean result = this.sessionService.linkClient(client, username);
            System.out.println("Client is linked: " + result);

        };
    }

    private void sentUpdateAllEvent(UpdateAllEvent event){
        UpdateAllEvent.UpdateAllEventBody body = (UpdateAllEvent.UpdateAllEventBody)event.getBody();
        server.getRoomOperations(body.getRoom().getId()).getClients().forEach(cl -> {
            if (!event.getExcludedClients().contains(cl))
                cl.sendEvent((new UpdateAllEvent()).getName(), event);
        });
    }

    private DataListener onGetRoomDataClient(){
        return (client, data, ackSender) -> {
            String roomId =  data.toString();
            System.out.println("Room with id: " + roomId);

            Room room = this.sessionService.getRoomById(roomId);
            client.sendEvent((new UpdateAllEvent()).getName(), new UpdateAllEvent(new UpdateAllEvent().new UpdateAllEventBody(room)));
            // this.sentUpdateAllEvent(new UpdateAllEvent(new UpdateAllEvent().new UpdateAllEventBody(room)));
        };
    }

    private DataListener onDisconnectClient(){
        return (client, data, ackSender) -> {
            User sender = ((DisconnectEvent.DisconnectEventBody)data).getSender();
            String roomId = this.sessionService.getClientRoom(sender).getId();
            this.sessionService.removeUser(sender);

            Room room = this.sessionService.getRoomById(roomId);
            this.sentUpdateAllEvent(new UpdateAllEvent(new UpdateAllEvent().new UpdateAllEventBody(room)));
        };
    }

    private DataListener onChatMesage(){
        return (client, data, ackSender) -> {
            Message message = ((ChatMessageEvent.ChatMessageEventBody)data).getMessage();
            this.sessionService.addMessage(message);

            Room room = this.sessionService.getClientRoom(message.getSender());
            this.sentUpdateAllEvent(new UpdateAllEvent(new UpdateAllEvent().new UpdateAllEventBody(room)));
        };
    }

    private DataListener onChooseWord(){
        return (client, data, ackSender) -> {
            User sender = ((ChooseWordEvent.ChooseWordEventBody)data).getSender();
            Word word = ((ChooseWordEvent.ChooseWordEventBody)data).getWord();
            this.sessionService.addChosenWord(sender, word);

            Room room = this.sessionService.getClientRoom(sender);
            this.sentUpdateAllEvent(new UpdateAllEvent(new UpdateAllEvent().new UpdateAllEventBody(room)));
        };
    }

    private DataListener onDraw(){
        return (client, data, ackSender) -> {
            User sender = ((DrawEvent.DrawEventBody)data).getSender();
            DrawnImage image = ((DrawEvent.DrawEventBody)data).getImage();
            this.sessionService.updateDrawnImage(sender, image);

            Room room = this.sessionService.getClientRoom(sender);
            UpdateAllEvent event = new UpdateAllEvent(new UpdateAllEvent().new UpdateAllEventBody(room));
            event.addExcludedClients(client); // exlude the client that is drawing so that he con continue to draw in peace
            this.sentUpdateAllEvent(event); 
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
}
