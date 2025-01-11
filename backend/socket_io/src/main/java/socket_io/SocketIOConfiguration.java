package socket_io;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

public class SocketIOConfiguration {
    private String host;
    private Integer port;
    private String origin;

    public SocketIOConfiguration(){};

    public SocketIOConfiguration(String host, Integer port){
        this.host = host;
        this.port = port;
        this.origin = "*";
    }

    public SocketIOConfiguration(String host, Integer port, String origin){
        this.host = host;
        this.port = port;
        this.origin = origin;
    }

    public SocketIOServer createSocketIOServer() {
        Configuration config = new Configuration();
        config.setHostname(host);
        config.setPort(port);
        config.setOrigin(origin);
        
        return new SocketIOServer(config);
    }
}
