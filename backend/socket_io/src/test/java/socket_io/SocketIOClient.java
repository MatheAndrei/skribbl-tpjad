package socket_io;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import socket_io.events.server.DisconnectEvent;


public class SocketIOClient {
    private String host;
    private Integer port;

    public Socket clientSocket;


    public SocketIOClient(String host, Integer port) throws URISyntaxException{
        this.host = host;
        this.port = port;
        IO.Options options = new IO.Options();
        options.reconnection = true;

        // IO.setDefaultHostnameVerifier((hostname, session) -> {
        //     System.out.println("Connecting to: " + hostname);
        //     return true;
        // });

        this.clientSocket = IO.socket("http://" + this.host + ":" + this.port, options);
        
        this.initClient();
    }

    public void connect(){
        this.clientSocket.connect();
    }

    public boolean isConnected(){
        return this.clientSocket.connected();
    }

    private void initClient(){
        this.clientSocket.on((new DisconnectEvent()).getName(), new Emitter.Listener() {
            @Override
            public void call(Object... args) {
            }
        });

        this.clientSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Connected to server!");
            }
        });
        this.clientSocket.on(Socket.EVENT_CONNECT_ERROR, args -> {
            System.err.println("Connection error: " + args[0]); // Log the error details
        });
    }
}
