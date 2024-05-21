import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private PrintWriter out;
    private int userID;
    private int chatRoomID;
    private Socket socket;
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try(
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ) {
            this.out = out;

            userID = Integer.parseInt(in.readLine()); //FIRST THING TO RECEIVE IS THE ID

            chatRoomID = Integer.parseInt(in.readLine()); //WILL REDEFINE LATER ONCE REDEFINED

        } catch(IOException e) {
            e.printStackTrace();
        }

    }
}
