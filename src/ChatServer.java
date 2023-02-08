import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
    ArrayList<Client> clients = new ArrayList<>();
    ServerSocket serverSocket;
    ChatServer() throws IOException {
        // создаем серверный сокет на порту 1234
        serverSocket = new ServerSocket(1234);
    }

    void sendAll (String sender, String message){
        for (Client client: clients){
            client.receive(message, sender);
        }
    }

    void sendTo (String sender,String receiver, String message){
        if (receiver.equalsIgnoreCase("All")){
            this.sendAll(sender, message);
        } else {
            for (Client client1 : clients) {
                if (client1.name.equals(receiver)) {
                    client1.receive (message, sender);
                }
            }
        }
    }
    public void run(){
        while(true) {
            System.out.println("Waiting...");
            // ждем клиента из сети
            try {
                // ждем клиента из сети
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");
                // создаем клиента на своей стороне
                clients.add(new Client(socket, this));

            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public boolean checkName (String name) {
        if (name.equalsIgnoreCase("bye") || name.equalsIgnoreCase("All"))return false;
        for (Client client2 : clients) {
            if (client2.name.equals(name)) {
                return false;
            }
        }
        return true;
    }



        public static void main (String[]args) throws IOException {

            new ChatServer().run();

        }
    }

