package socket_io;

import java.net.URISyntaxException;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.corundumstudio.socketio.SocketIOServer;

public class TestSocketIoService {
    private SocketIOServer server;
    private String testingHost = "localhost";
    private Integer testingPort = 9093;
    private SocketIOService service;
    private SocketIOClient client;

    @BeforeEach
    void startServer() {
        server = new SocketIOConfiguration(testingHost,testingPort).createSocketIOServer();
        service = new SocketIOService(server);
        
        service.start();
    }

    @AfterEach
    void stopServer() {
        service.stop();
    }

    // @Test
    // void testConnection() throws URISyntaxException, InterruptedException {
    //     // Connect to the server
    //     client = new SocketIOClient(testingHost, testingPort);
    //     client.connect();
    //     Thread.sleep(1000);
    //     assertTrue(client.isConnected());
    // }

    // @Test
    // void testSocketIoEventHandling() throws URISyntaxException, InterruptedException {
    //     // Connect to the server using a socket.io client
    //     Socket client = IO.socket("http://" + testingHost + ":" + testingPort);

    //     CountDownLatch latch = new CountDownLatch(1);
    //     final StringBuilder response = new StringBuilder();

    //     // Listen for the server's response
    //     client.on("testResponse", new Emitter.Listener() {
    //         @Override
    //         public void call(Object... args) {
    //             response.append((String) args[0]);
    //             latch.countDown();
    //         }
    //     });

    //     // Connect to the server
    //     client.connect();
    //     assertTrue(client.connected());

    //     // Emit an event to the server
    //     client.emit("testEvent", "World");

    //     // Wait for the server response
    //     boolean success = latch.await(5, TimeUnit.SECONDS);
    //     assertTrue(success, "The server did not respond in time");
    //     assertEquals("Hello World", response.toString());

    //     client.disconnect();
    // }
}
