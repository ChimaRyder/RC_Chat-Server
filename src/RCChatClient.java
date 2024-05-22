import java.io.*;
import java.net.*;

public class RCChatClient {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 1803;

        try (
                Socket socket = new Socket(serverAddress, port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        ) {
//            system.out.println(in.readline());  // enter your identifier
//            string userid = stdin.readline();
//            out.println(userid);
//
//            system.out.println(in.readline());  // enter chat room id
//            string chatroomid = stdin.readline();
//            out.println(chatroomid);
//

//
//
            out.println(1);
            out.println(4156);

            // start a thread to listen for messages from the server
            new Thread(new IncomingReader(in)).start();

            String userinput;
            while ((userinput = stdIn.readLine()) != null) {
                out.println(userinput);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class IncomingReader implements Runnable {
        private BufferedReader in;

        public IncomingReader(BufferedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            String fromServer;
            try {
                while ((fromServer = in.readLine()) != null) {
                    System.out.println(fromServer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
