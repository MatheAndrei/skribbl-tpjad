package socket_io;

import com.corundumstudio.socketio.SocketIOServer;

/**
 * Only for testing purposes!!!
 */
public class App {
    public static void main(String[] args) {
        String testingHost = "localhost";
        Integer testingPort = 9092;
        SocketIOServer server = new SocketIOConfiguration(testingHost,testingPort).createSocketIOServer();
        SocketIOService service = new SocketIOService(server);
        Runtime.getRuntime().addShutdownHook(new Thread(service::stop));
        
        service.start();

        try {
            Thread.sleep(Long.MAX_VALUE); // Keep the server running
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
