import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class RCChatServer {
    private static HashMap<Integer, Set<ClientHandler>> activechats = new HashMap<>();
    private static final int PORT = 1803;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) { // creates a new serverSocket for any clients to connect to

            System.out.println("SERVER ACTIVE. LISTENING TO PORT " + PORT);

            while (true) {
                Socket socket = serverSocket.accept(); //keeps on running, checking if any users connect
                new Thread(new ClientHandler(socket)); // once connected, it accepts the socket and places it onto a ClientHandler
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
