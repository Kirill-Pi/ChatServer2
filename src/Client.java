import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

class Client implements Runnable {
    Socket socket;

    Scanner in;
    PrintStream out;
    ChatServer server;
    String name = " ";

    public Client(Socket socket, ChatServer server){

        this.socket = socket;
        this.server = server;
        // запускаем поток
        new Thread (this).start();
    }

    void receive (String message, String name){
        out.println("Message from "+ name + ": " + message);


    }

    public void run() {
        try {
            // получаем потоки ввода и вывода
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            // создаем удобные средства ввода и вывода
            in = new Scanner(is);
            out = new PrintStream(os);

            // читаем из сети и пишем в сеть
            out.println("Welcome to chat!");
            out.println("Please enter your name:");
            this.setName();
            server.sendAll("Chat: ",this.name + " joined the Chat");
            server.sendTo("Chat: ", name, "Enter receiver name or All:");
            String input = in.nextLine();
            String tempName1 = input;
            while (!(input.equals("bye") || tempName1.equals("bye"))){
                tempName1 = input;
                while (server.checkName(tempName1)){
                    out.println("Incorrect receiver name. Please try again:");
                    tempName1 = in.nextLine();
                }
                server.sendTo("Chat: ", name, "Enter message:");
                input = in.nextLine();
                server.sendTo(this.name, tempName1, input);
                server.sendTo("Chat: ", name, "Enter receiver name or All:");
                input = in.nextLine();
            }
            server.sendAll("Chat", this.name + " left the Chat");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setName (){
        String tempName = in.nextLine();
        out.println(tempName);
        while (!server.checkName (tempName)){
            if (server.checkName (tempName)) break;
            else out.println("Incorrect name. Please try again:");
            tempName = in.nextLine();
        }
        this.name = tempName;
    }
}
