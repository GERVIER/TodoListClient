/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensim.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import model.Tache;
import model.User;

/**
 *
 * @author Rémy
 */
public class networkHandler {

	private static Socket aClient = null;

	private static boolean serverOnline = false;
	private static DataOutputStream dout = null;

	private static BufferedReader din = null;

	private static ObjectInputStream ois = null;
	private static ObjectOutputStream oos = null;

	public static void init() {
		try {
			aClient = new Socket("localhost", 1500);
			serverOnline = true;

			// Entrée
			din = new BufferedReader(new InputStreamReader(aClient.getInputStream()));

			// Sortie
			dout = new DataOutputStream(aClient.getOutputStream());
			
		} catch (IOException ex) {
			ex.getMessage();
			serverOnline = false;
		}
	}

	public static void prerareObjectRcv() {
		try {
			ois = new ObjectInputStream(aClient.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void prerareObjectSend() {
		try {
			oos = new ObjectOutputStream(aClient.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
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
	 * 
	 * @return L'état du serveur
	 */
	public static boolean isServerOnline() {
		return serverOnline;
	}

	/**
	 * Envoie un message text au serveur
	 * 
	 * @param msg
	 *            le message à envoyer
	 */
	public static void sendMsgToServ(String msg) {
		if (serverOnline) {
			System.out.println("Message envoyé au serveur: " + msg);
			try {
				dout.writeBytes(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Recois un message du serveur
	 * 
	 * @return le message recu
	 */
	public static String rcvMsgFromServ() {
		if (serverOnline) {
			try {
				return din.readLine();
			} catch (IOException ex) {
				System.out.println("ERROR 42: I'M A UNITATO !");
			}
			return "ERROR 42: I'M A UNITATO !";
		}
		return "DEMOMODE";
	}

	public static void sendTaskToServ(Tache t, String typeOP) {
		if (serverOnline) {
			try {
				sendMsgToServ(typeOP);
				Tache tache = null;
				if (t != null) {
					tache = new Tache(t.titre, t.tacheID, t.texte, t.priorite, t.etat, t.dateFin, t.dateCreation,
							t.idCreateur, t.idRealisateur);
				}
				prerareObjectSend();
				oos.writeObject(tache);
				oos.flush();
				System.out.println("\nTache envoyé: \n" + tache);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void sendUserToServ(User u) {
		if (serverOnline) {
			try {
				User user = new User(u.userID, u.nom, u.prenom, u.mail, u.mdp);
				prerareObjectSend();
				oos.writeObject(user);
				oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static User rvcUserFromServ() {
		prerareObjectRcv();
		User u = null;
		if (serverOnline) {
			try {
				u = (User) ois.readObject();
				System.out.println(u);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
		return u;
	}
}
