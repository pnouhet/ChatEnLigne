package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
	
    public static List<ClientThread> clients = new CopyOnWriteArrayList<>();

    public static void main(String args[]) {
        ServerSocket serv;
        try {
            int port = 5588;
            serv = new ServerSocket(port);
            System.out.println("Serveur démarré sur le port " + port);
            
            while (true) {
            	Socket clientSocket = serv.accept();
                System.out.println("Nouveau client connecté");
                ClientThread cli = new ClientThread(clientSocket);
                clients.add(cli);
                cli.start();
            }
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
}