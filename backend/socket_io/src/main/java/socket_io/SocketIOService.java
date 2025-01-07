package socket_io;

import java.util.List;
import java.util.stream.Collectors;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import domain.DrawnImage;
import domain.Message;
import domain.Room;
import domain.User;
import domain.Word;
import socket_io.events.client.UpdateAllEvent;
import socket_io.events.client.UpdateAllEvent.UpdateAllEventBody;
import socket_io.events.server.ChatMessageEvent;
import socket_io.events.server.ChooseWordEvent;
import socket_io.events.server.DisconnectEvent;
import socket_io.events.server.DrawEvent;

public class SocketIOService {
    private final SocketIOServer server;
    private final SessionService sessionService;

    public SocketIOService(SocketIOServer server, SessionService sessionService) {
        this.server = server;
        this.sessionService = sessionService;
        this.initializeServer();
    }
    
    private void initializeServer(){
        this.server.addDisconnectListener(this.onDisconnect());
        this.server.addEventListener(DisconnectEvent.getName(), DisconnectEvent.DisconnectEventBody.class, this.onDisconnectClient());
        this.server.addEventListener(ChatMessageEvent.getName(), ChatMessageEvent.ChatMessageEventBody.class, this.onChatMesage());
        this.server.addEventListener(ChooseWordEvent.getName(), ChooseWordEvent.ChooseWordEventBody.class, this.onChooseWord());
        this.server.addEventListener(DrawEvent.getName(), DrawEvent.DrawEventBody.class, this.onDraw());
    }

    private DisconnectListener onDisconnect(){
        return client -> {
            var params = client.getHandshakeData().getUrlParams();
            String room = params.get("room").stream().collect(Collectors.joining());
            String username = params.get("username").stream().collect(Collectors.joining());

            this.sessionService.removeClient(client);
        };
    }

    private void sentUpdateAllEvent(UpdateAllEvent event){
        UpdateAllEvent.UpdateAllEventBody body = (UpdateAllEvent.UpdateAllEventBody)event.getBody();
        List<SocketIOClient> clients = this.sessionService.getSocketIOClientsForRoom(body.getRoom());
        for (var client : clients){
            if (!event.getExcludedClients().contains(client))
                client.sendEvent(UpdateAllEvent.getName(), event);
        }
    }

    private DataListener onDisconnectClient(){
        return (client, data, ackSender) -> {
            User sender = ((DisconnectEvent.DisconnectEventBody)data).getSender();
            String roomId = this.sessionService.getClientRoom(sender).getId();
            this.sessionService.removeClient(sender);

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
}
