package springboot.socket_io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

import com.corundumstudio.socketio.SocketIOServer;

import domain.User;
import springBoot.socket_io.SessionService;
import springBoot.socket_io.SocketIOConfiguration;
import springBoot.socket_io.SocketIOService;

@SpringBootTest
@ActiveProfiles("test")
public class TestSocketIoService {
    @Value("${socket-server.host}")
    private String testingHost;
    @Value("${socket-server.port}")
    private Integer testingPort;
    @Autowired
    private SessionService serviceS;
    //@Autowired
    // private SocketIOService service;

    //private SocketIOClient client;
    // private Thread serverThread;

    // private boolean isPortFree(String host, int port) {
       
            
    //         try (Socket socket = new Socket()) {
    //             socket.connect(new InetSocketAddress(host, port), 100); // Timeout of 100 ms
    //             System.out.println("Port " + port + " is in use.");
    //         } catch (IOException e) {
    //             // If an IOException occurs, the port is considered free
    //             System.out.println("Port " + port + " is free.");
    //             return true;
    //         }
            
    //     return false; // Port is still in use after retries
    // }

    // @BeforeEach
    // void startServer() {
    //     serverThread = new Thread(() -> {
    //         int maxRetries = 5;
    //         int retryCount = 0;
    //         try {
    //             server = new SocketIOConfiguration(testingHost, testingPort).createSocketIOServer();
    //             serviceS = new SessionService();
    //             service = new SocketIOService(server, serviceS);
    //             while (retryCount < maxRetries) {
    //                 service.start();
    //                 try {
    //                     Thread.sleep(1000); 
    //                 } catch (InterruptedException ie) {
    //                     System.err.println("Thread interrupted while waiting: " + ie.getMessage());
    //                 }
    //                 retryCount++;
    //             }
    //             while (!Thread.currentThread().isInterrupted()) {
    //                 Thread.sleep(100); // Check periodically for interruption
    //             }

    //         } catch (InterruptedException e) {
    //             service.stop();
    //             Thread.currentThread().interrupt();
    //         } catch (Exception e) {
    //             throw new RuntimeException("Error while starting the server", e);
    //         }
    //     });
    
    //     serverThread.start();
    //     try {
	// 		Thread.currentThread().sleep(1000);
	// 	} catch (InterruptedException e) {
	// 		e.printStackTrace();
	// 	} 
    // }

    // @AfterEach
    // void stopServer() {
    //     if (serverThread != null && serverThread.isAlive()) {
    //         serverThread.interrupt();
    //         try {
    //             serverThread.join();
    //             Thread.currentThread().sleep(1000);
    //         } catch (InterruptedException e) {
    //             Thread.currentThread().interrupt();
    //             e.printStackTrace();
    //             throw new RuntimeException("Error while stopping the server", e);
    //         }
    //     }
    // }

    @BeforeEach
    void resetService(){
        this.serviceS.reset();
    }

    @Test
    void testConnection() throws InterruptedException, URISyntaxException {
        SocketIOClient client = new SocketIOClient(testingHost, testingPort);
        client.connect("aaa");
    
        Thread.sleep(1000); 
        assertTrue(client.isConnected(), "Client failed to connect.");
    }

    @Test
    void testConnectionJoin() throws InterruptedException, URISyntaxException {
        String testUsername = "aaa";
        User user = this.serviceS.createUser(testUsername);
        SocketIOClient client = new SocketIOClient(testingHost, testingPort);
        client.connect("aaa");
        Thread.sleep(1000); 

        user = this.serviceS.getUserByUsername(testUsername);
        
        assertTrue(client.isConnected(), "Client failed to connect.");
        assertNotNull(user.getId());
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
