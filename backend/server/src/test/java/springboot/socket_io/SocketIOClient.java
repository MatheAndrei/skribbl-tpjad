package springboot.socket_io;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class SocketIOClient {
    private String host;
    private Integer port;

    public Socket clientSocket;


    public SocketIOClient(String host, Integer port){
        this.host = host;
        this.port = port;
        
        
        
    }

    public void connect(String username) throws URISyntaxException{
        IO.Options options = new IO.Options();
        options.reconnection = true;
        options.query = "username=" + username;

        var url = "http://" + this.host + ":" + this.port;
        this.clientSocket = IO.socket(url, options);
        this.initClient();
        this.clientSocket.connect();
        
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
    }
}
