package application;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String args[]) throws IOException {

        ServerSocket serverSocket = new ServerSocket(6789);
        System.out.println("Listening on port 6789");

        new MenuThread().start();

        while (true) {
            Socket socket = serverSocket.accept();

            // new thread for client
            new EchoThread(socket).start();

            InetAddress ip = socket.getInetAddress();
            int port = socket.getPort();

            System.out.println("New client connected from: " +ip + ":" + port);
        }
    }
}
