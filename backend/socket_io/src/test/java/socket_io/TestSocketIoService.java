package socket_io;

import java.net.URISyntaxException;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.corundumstudio.socketio.SocketIOServer;
import springBoot.socket_io.SocketIOConfiguration;
import springBoot.socket_io.SocketIOService;

public class TestSocketIoService {
    private SocketIOServer server;
    private String testingHost = "localhost";
    private Integer testingPort = 9093;
    private SocketIOService service;
    private SocketIOClient client;
    private Thread serverThread;

    @BeforeEach
    void startServer() {
        serverThread = new Thread(() -> {
            try {
                server = new SocketIOConfiguration(testingHost, testingPort).createSocketIOServer();
                service = new SocketIOService(server);
                service.start();
                
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(100); // Check periodically for interruption
                }
            } catch (InterruptedException e) {
                service.stop();
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                throw new RuntimeException("Error while starting the server", e);
            }
        });
    
        serverThread.start();
        try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
    }

    @AfterEach
    void stopServer() {
        if (serverThread != null && serverThread.isAlive()) {
            serverThread.interrupt();
            try {
                serverThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Error while stopping the server", e);
            }
        }
    }

    @Test
    void testConnection() throws InterruptedException, URISyntaxException {
        client = new SocketIOClient(testingHost, testingPort);
        client.connect();
    
        Thread.sleep(1000); 
        assertTrue(client.isConnected(), "Client failed to connect.");
    }

    

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
