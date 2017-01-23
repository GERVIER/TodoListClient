/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensim.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * @author Rémy
 */
public class networkHandler {

    private static Socket aClient = null;

    private static boolean serverOnline = false;
    private static InputStream in = null;
    private static OutputStream out = null;
    
    private static BufferedReader din = null;
    private static ObjectInputStream ois = null;
    private static PrintStream pout = null;
    
    

    public static void init() {
        try {
            aClient = new Socket("localhost", 8540);
            serverOnline = true;
            in = aClient.getInputStream();
            din = new BufferedReader(new InputStreamReader(in));
            ois = new ObjectInputStream(aClient.getInputStream());
            
            
            out = aClient.getOutputStream();
            pout = new PrintStream(out);
        } catch (IOException ex) {
            ex.getMessage();
            serverOnline = false;
        }
    }

    /**
     * Retourne le socket client pour communiquer avec le serveur
     *
     * @return aClient: socket de communication
     */
    public static Socket getSocket() {
        return aClient;
    }

    /**
     * Indique si le serveur est en ligne ou non.
     * @return L'état du serveur
     */
    public static boolean isServerOnline() {
        return serverOnline;
    }

    /**
     * Envoie un message text au serveur
     * @param msg le message à envoyer
     */
    public static void sendMsgToServ(String msg) {
        if (serverOnline) {
            System.out.println("Message envoyé au serveur: " + msg);
            pout.println(msg);
        }
    }

    /**
     * Recois un message du serveur
     * @return le message recu
     */
    public static String rcvMsgFromServ() {
        try {
            return din.readLine();
        } catch (IOException ex) {
            System.out.println("ERROR 42: I'M A UNITATO !");
        }
        return "ERROR 42: I'M A UNITATO !";
    }
    
    
    
}
