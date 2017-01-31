/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensim.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Map.Entry;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Tache;
import model.User;

/**
 * FXML Controller class
 *
 * @author Rémy
 */
public class ListeTachesController implements Initializable {
	public static Map<String, String> usersList;
	
	private static User user = null;

	@FXML
	VBox vbox_lstTask;
	@FXML
	VBox vbox_lstTaskToDo;
	@FXML
	Button bt_addTask;
	@FXML
	Button bt_actualise;
	@FXML
	Button bt_deco;
	@FXML
	Label lb_bonjour;

	ArrayList<Tache> taskList = new ArrayList<Tache>();
	ArrayList<Tache> taskListToDo = new ArrayList<Tache>();

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		bt_addTask.setOnAction(ChangeToCreateMode);
		bt_actualise.setOnAction(Refresh);
		bt_deco.setOnAction(deconnexion);

		if (networkHandler.isServerOnline()) {
			// Récupération des taches par le réseaux
			if (user == null) { //Si 1er co
				user = networkHandler.rvcUserFromServ();

				if (user == null) { //Si erreur => renvoie page de co
					Stage stage;
					stage = (Stage) bt_addTask.getScene().getWindow();
					try {
						switchToView("/fxml/EcranConnexion.fxml", stage);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					taskList = user.lstTachesCrea;
					taskListToDo = user.lstTachesRea;
				}

			} else { //Si actualisation
				networkHandler.sendMsgToServ("ACTUALISATION\n");
				networkHandler.sendMsgToServ("" + user.userID + "\n");

				user = networkHandler.rvcUserFromServ();
				taskList = user.lstTachesCrea;
				taskListToDo = user.lstTachesRea;
			}

			//Récup liste de tout le monde
			usersList = networkHandler.rcvUserList();

			Set<Entry<String, String>> setHm = usersList.entrySet();
			Iterator<Entry<String, String>> iterator = setHm.iterator();

			while (iterator.hasNext()) {
				Entry<String, String> e = iterator.next();
				System.out.println(e.getKey() + " : " + e.getValue());
			}

		}

		//Affichage des taches
		try {
			for (Tache t : taskList) {
				AddTask(t, vbox_lstTask, "Crea");
			}

			for (Tache t : taskListToDo) {
				AddTask(t, vbox_lstTaskToDo, "Rea");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		lb_bonjour.setText("Bienvenue " + user.nom + " " + user.prenom + "!" + " Mail: " + usersList.get(user.userID));

	}

	/**
	 * Ajoute une tache a la liste des taches
	 * 
	 * @param task
	 *            : tache à ajouter a la liste des taches
	 * @throws IOException
	 */
	public void AddTask(Tache task, VBox boxToAdd, String type) throws IOException {
		BorderPane p = FXMLLoader.load(getClass().getResource("/fxml/Tache.fxml"));
		BorderPane top = (BorderPane) p.getTop();
		BorderPane center = (BorderPane) p.getCenter();
		BorderPane center_center = (BorderPane) center.getCenter();

		// Récupération du HBox avec les deux buttons
		HBox top_left = (HBox) top.getRight();
		// Récupération du button edit
		Button edit = (Button) top_left.getChildren().get(0);

		edit.setOnAction(ChangeToEditMode);

		// Récupération du button suppr
		Button suppr = (Button) top_left.getChildren().get(1);
		suppr.setOnAction(RemoveTask);

		// Récupération et application du titre:
		Label titre = (Label) ((GridPane) top.getCenter()).getChildren().get(0);
		titre.setText(task.titre);

		// Récupération et application de la date, du mec qui doit bosser/qui a
		// creée et de la priorité
		HBox center_center_top = (HBox) center_center.getTop();
		Label date = (Label) center_center_top.getChildren().get(1);
		Label text_rea = (Label) center_center_top.getChildren().get(3);
		Label realisateur = (Label) center_center_top.getChildren().get(4);
		Label priority = (Label) center_center_top.getChildren().get(7);
		Label etat = (Label) center_center_top.getChildren().get(10);

		date.setText(task.dateFin.toString());
		if (type.equals("Crea")) {
			realisateur.setText(usersList.get(task.idRealisateur));
		} else {
			text_rea.setText("Créateur: ");
			realisateur.setText(usersList.get(task.idCreateur));
		}

		priority.setText(task.priorite);
		etat.setText(task.etat);

		// Récupération et application du texte
		TextArea desc = (TextArea) center_center.getCenter();
		desc.setText(task.texte);

		// Finition et ajout de la tache
		TitledPane t = new TitledPane(task.titre + ". Pour le " + task.dateFin.toString(), p);
		t.getStyleClass().add("titreTache");
		boxToAdd.getChildren().add(t);
	}

	/**
	 * 
	 */
	public EventHandler<ActionEvent> RemoveTask = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {

			Stage stage;
			Button b = (Button) event.getSource();
			stage = (Stage) b.getScene().getWindow();

			TitledPane tp = (TitledPane) b.getParent().getParent().getParent().getParent().getParent();
			VBox box = (VBox) tp.getParent();

			int i = 0;
			for (Node n : box.getChildren()) {
				if (tp.equals(n))
					break;
				else
					i++;
			}

			if (box.getId().equals("vbox_lstTaskToDo"))
				networkHandler.sendTaskToServ(taskListToDo.get(i), "SUPPRESSION\n");
			else
				networkHandler.sendTaskToServ(taskList.get(i), "SUPPRESSION\n");

			try {
				switchToView("/fxml/ListeTaches.fxml", stage);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * 
	 */
	public EventHandler<ActionEvent> ChangeToCreateMode = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			Stage stage;
			Button b = (Button) event.getSource();
			stage = (Stage) b.getScene().getWindow();

			TaskEditorHandler.setTacheToEdit(
					new Tache("", "", "", "Peu Urgent", "En cours", LocalDate.now(), LocalDate.now(), user.userID, ""));
			try {
				switchToView("/fxml/TacheEdition.fxml", stage);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * 
	 */
	public EventHandler<ActionEvent> ChangeToEditMode = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			Stage stage;
			Button b = (Button) event.getSource();
			TitledPane tp = (TitledPane) b.getParent().getParent().getParent().getParent().getParent();
			VBox box = (VBox) tp.getParent();

			int i = 0;
			for (Node n : box.getChildren()) {
				if (tp.equals(n))
					break;
				else
					i++;
			}

			String nextView;
			if (box.getId().equals("vbox_lstTaskToDo")) {
				TaskEditorHandler.setTacheToEdit(taskListToDo.get(i));
				nextView = "/fxml/TacheEditionRea.fxml";
			}

			else {
				TaskEditorHandler.setTacheToEdit(taskList.get(i));
				nextView = "/fxml/TacheEdition.fxml";
			}

			stage = (Stage) b.getScene().getWindow();
			try {
				switchToView(nextView, stage);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	public EventHandler<ActionEvent> Refresh = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {

			Stage stage;
			Button b = (Button) event.getSource();
			stage = (Stage) b.getScene().getWindow();

			try {
				switchToView("/fxml/ListeTaches.fxml", stage);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	EventHandler<ActionEvent> deconnexion = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			Stage stage;
			Button b = (Button) event.getSource();
			stage = (Stage) b.getScene().getWindow();

			try {
				user = null;
				switchToView("/fxml/EcranConnexion.fxml", stage);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * Permet de changer de vue
	 *
	 * @param view
	 *            La nouvelle vue voulu
	 * @param stage
	 *            La fenetre pour la quelle la vue est à changer
	 * @throws IOException
	 */
	public void switchToView(String view, Stage stage) throws IOException {
		Parent newView;
		double h = stage.getHeight();
		double w = stage.getWidth();

		newView = FXMLLoader.load(getClass().getResource(view));
		Scene scene = new Scene(newView);

		stage.setScene(scene);
		stage.setHeight(h);
		stage.setWidth(w);
	}

}
