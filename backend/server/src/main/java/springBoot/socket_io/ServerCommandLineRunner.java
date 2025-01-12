package springBoot.socket_io;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOServer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ServerCommandLineRunner implements CommandLineRunner {

    private final SocketIOService server;

    @Override
    public void run(String... args) throws Exception {
        server.start();
    }
}
