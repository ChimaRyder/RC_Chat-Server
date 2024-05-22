import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class RCChatServer {
    private static HashMap<Integer, Set<ClientHandler>> chatRooms = new HashMap<>();
    private static ArrayList<ClientHandler> waitingClients = new ArrayList<>();
    private static final int PORT = 1803;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) { // creates a new serverSocket for any clients to connect to

            System.out.println("SERVER ACTIVE. LISTENING TO PORT " + PORT);

            while (true) {
                Socket socket = serverSocket.accept(); //keeps on running, checking if any users connect
                new Thread(new ClientHandler(socket)).start(); // once connected, it accepts the socket and places it onto a ClientHandler
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static class ClientHandler extends Thread {
        private final Socket socket;
        private PrintWriter out;
        private String clientId;
        private int chatRoomId;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ) {
                this.out = out;

                // we get this client first before nato icheck iyang codes
                clientId = in.readLine();

                // Add client to the chat room
//                synchronized (chatRooms) {
//                    chatRooms.computeIfAbsent(chatRoomId, k -> new HashSet<>()).add(this);
//                }
//                out.println("Welcome " + clientId + " to chat room " + chatRoomId);

//                String inputLine;
//                while ((inputLine = in.readLine()) != null) {
//                    sendMessageToChatRoom(chatRoomId, clientId + ": " + inputLine);
//                }

                //wait for a command
                int code;
                while (true) {
                    code = Integer.parseInt(in.readLine());
                    switch (code) {
                        case 4156: //start chatroom button
                            StartChat(in);
                            break;
                        case 9104:
                            out.println("You selected 9104");
                            break;
                    }
                }
            } catch (SocketException s) {
                sendMessageToChatRoom(chatRoomId, clientId + " has disconnected");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Cleanup when client disconnects
//                synchronized (chatRooms) {
//                    Set<ClientHandler> clients = chatRooms.get(chatRoomId);
//                    if (clients != null) {
//                        clients.remove(this);
//                        if (clients.isEmpty()) {
//                            chatRooms.remove(chatRoomId);
//                        }
//                    }
//                }
//                try {
//                    socket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }

        private void StartChat(BufferedReader in) {
            synchronized (waitingClients) {
                waitingClients.add(this);
            }

            out.println("Connecting you with someone...");
            while (true) {
                Random r = new Random();
                int other_client = r.nextInt(waitingClients.size());
                synchronized (waitingClients) {
                    if (!waitingClients.contains(this)){
                        break;
                    } else if (waitingClients.get(other_client) != this) {
                        Set<ClientHandler> new_room_clients = new HashSet<>();
                        new_room_clients.add(this);
                        new_room_clients.add(waitingClients.get(other_client));

                        synchronized (chatRooms) {
                            chatRooms.put(chatRooms.size() + 1, new_room_clients);
                            chatRoomId = chatRooms.size();
                            waitingClients.remove(other_client);
                            waitingClients.remove(this);
                            System.out.println("new room created");
                            out.println("You have connected with someone. Start Chatting!");
                        }

                        break;
                    }
                }
            }

           try {
               String inputLine;
               while ((inputLine = in.readLine()) != null) {
                   if (inputLine.startsWith("8430!")) {
                       return;
                   }

                   sendMessageToChatRoom(chatRoomId, inputLine);
               }
           } catch (IOException e) {
               e.printStackTrace();
           }
        }

        private void sendMessageToChatRoom(int chatRoomId, String message) {
            synchronized (chatRooms) {
                Set<ClientHandler> clients = chatRooms.get(chatRoomId);
                if (clients != null) {
                    for (ClientHandler client : clients) {
                        client.out.println(message);
                    }
                }
            }
        }
    }
}
