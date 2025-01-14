package springboot.socket_io;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import domain.Room;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import springBoot.socket_io.events.client.UpdateAllEvent;
import springBoot.socket_io.events.client.UpdateAllEventBody;
import springBoot.socket_io.events.server.JoinRoomEvent;


public class SocketIOClient {
    private String host;
    private Integer port;

    public Socket clientSocket;
    public Room room = null;
    public CountDownLatch latch;


    public SocketIOClient(String host, Integer port){
        this.host = host;
        this.port = port;
        this.room = null;
    }

    public void connect(String username) throws URISyntaxException{
        IO.Options options = new IO.Options();
        options.reconnection = true;
        options.query = "username=" + username;

        var url = "http://" + this.host + ":" + this.port;
        this.clientSocket = IO.socket(url, options);
        this.clientSocket.connect();
        latch = new CountDownLatch(10);
        this.initClient();

        
    }

    public boolean isConnected(){
        return this.clientSocket.connected();
    }

    private void initClient(){
        this.clientSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Disconnected from the server!");
            }
        });

        this.clientSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Connected to server!");
            }
        });
        this.clientSocket.on(Socket.EVENT_CONNECT_ERROR, args -> {
            System.err.println("Connection error: " + args[0]);
        });

        this.clientSocket.on(new UpdateAllEvent().getName(), args -> {
            System.out.println("Raw event data: " + Arrays.toString(args));
            this.room = ((UpdateAllEventBody)args[0]).getRoom();
            latch.countDown();
            System.err.println("Update all");
        });
    }
}
