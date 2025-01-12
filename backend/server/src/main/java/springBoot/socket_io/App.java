package springBoot.socket_io;

import com.corundumstudio.socketio.SocketIOServer;

import domain.Room;

/**
 * Only for testing purposes!!!
 */
public class App {
    public static void main(String[] args) {
        String testingHost = "localhost";
        Integer testingPort = 9092;
        SocketIOServer server = new SocketIOConfiguration(testingHost,testingPort).createSocketIOServer();
        SessionService serviceS = new SessionService();
        SocketIOService service = new SocketIOService(server, serviceS);
        Runtime.getRuntime().addShutdownHook(new Thread(service::stop));

        var user1 = serviceS.createUser("aaa");
        var user2 = serviceS.createUser("bbb");
        Room room = serviceS.createRoom(user1);
        System.out.println(room.getId());

        service.start();

        try {
            Thread.sleep(Long.MAX_VALUE); // Keep the server running
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
