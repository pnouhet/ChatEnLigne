package main;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread extends Thread {
    
    private Socket client;
    private DataOutputStream outchan;
    private String pseudo;

    public ClientThread(Socket c) {
        this.client = c;
    }
    
    public void sendMessage(String msg) {
        try {
            outchan.write((msg + "\n").getBytes("UTF-8"));
            outchan.flush();
        } catch (IOException e) {
        	System.out.println("Erreur.");
        }
    }
    
    public void broadcast(String msg) {
        for (ClientThread otherClient : Server.clients) {
            if (otherClient != this) {
                otherClient.sendMessage(msg);
            }
        }
    }

    public void run() {
        BufferedReader inchan;
        try {
            inchan = new BufferedReader(new InputStreamReader(client.getInputStream()));
            outchan = new DataOutputStream(client.getOutputStream());
            sendMessage("Bienvenue ! Entrez votre pseudo :");
            this.pseudo = inchan.readLine();
            sendMessage("Bonjour " + this.pseudo + ", vous pouvez chatter.");
            broadcast("SERVEUR : " + this.pseudo + " a rejoint la discussion.");
            
            while (true) {
                String message = inchan.readLine();
                if (message == null || message.equalsIgnoreCase("exit")) {
                    break;
                }
                broadcast(this.pseudo + " : " + message);
            }
            
        } catch (IOException e) {
            System.err.println("Le client " + this.pseudo + " s'est déconnecté.");
        } finally {
        	
            try {
                Server.clients.remove(this);
                broadcast("SERVEUR : " + this.pseudo + " est parti.");
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}