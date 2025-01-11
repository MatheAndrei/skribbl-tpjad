package springBoot.socket_io;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class SocketIOConfiguration {
    @Value("${socket-server.host}")
    private String host;
    @Value("${socket-server.port}")
    private Integer port;
    @Value("${socket-server.origin}")
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

    @Bean
    public SocketIOServer createSocketIOServer() {
        Configuration config = new Configuration();
        config.setHostname(host);
        config.setPort(port);
        config.setOrigin(origin);
        
        return new SocketIOServer(config);
    }
}
