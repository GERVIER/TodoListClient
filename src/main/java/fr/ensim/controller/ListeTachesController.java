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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

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

	private static User user = null;
	
	@FXML
	VBox vbox_lstTask;
	@FXML
	Button bt_addTask;

	ArrayList<Tache> taskList = new ArrayList<Tache>();

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		bt_addTask.setOnAction(ChangeToCreateMode);

		if (networkHandler.isServerOnline()) {
			// Récupération des taches par le réseaux
			if (user == null) {
				user = networkHandler.rvcUserFromServ();
				taskList = user.lstTachesCrea;
				System.out.println(taskList.size());
			} else {
				networkHandler.sendMsgToServ("ACTUALISATION\n");
				networkHandler.sendMsgToServ(""+user.userID+"\n");
				System.out.println("Reponse: " + networkHandler.rcvMsgFromServ());
				user = networkHandler.rvcUserFromServ();
				taskList = user.lstTachesCrea;
				/*
				 * 
				 * ACTUALISATION A FAIRE
				 */
			}
		}

		try {
			for (Tache t : taskList) {
				AddTask(t);
			}
		} catch (IOException ex) {
			Logger.getLogger(ListeTachesController.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		}

	}

	/**
	 * Ajoute une tache a la liste des taches
	 * 
	 * @param task
	 *            : tache à ajouter a la liste des taches
	 * @throws IOException
	 */
	public void AddTask(Tache task) throws IOException {
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
		Label realisateur = (Label) center_center_top.getChildren().get(4);
		Label priority = (Label) center_center_top.getChildren().get(7);


		date.setText(task.dateFin.toString());
		realisateur.setText(task.idRealisateur);
		priority.setText(task.priorite);

		// Récupération et application du texte
		TextArea desc = (TextArea) center_center.getCenter();
		desc.setText(task.texte);

		// Finition et ajout de la tache
		TitledPane t = new TitledPane(task.tacheID + " : " + task.titre + ". Pour le " + task.dateFin.toString(),
				p);
		t.getStyleClass().add("titreTache");
		vbox_lstTask.getChildren().add(t);

	}

	/**
	 * 
	 */
	public EventHandler<ActionEvent> RemoveTask = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			System.err.println("REMOVING NOT IMPLEMENTED ! ");
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
					new Tache("", "", "", "Peu Urgent", "En cours", LocalDate.now(), LocalDate.now(), "", ""));
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

			int i = 0;
			for (Node n : vbox_lstTask.getChildren()) {
				if (tp.equals(n))
					break;
				else
					i++;
			}

			TaskEditorHandler.setTacheToEdit(taskList.get(i - 1));

			stage = (Stage) b.getScene().getWindow();
			try {
				switchToView("/fxml/TacheEdition.fxml", stage);
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
