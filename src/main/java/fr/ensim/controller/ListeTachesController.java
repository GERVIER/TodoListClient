/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensim.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
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

/**
 * FXML Controller class
 *
 * @author Rémy
 */
public class ListeTachesController implements Initializable {

	@FXML
	VBox vbox_lstTask;

	ArrayList<Tache> taskList = new ArrayList<Tache>();

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		if (!networkHandler.isServerOnline()) {
			// Récupération des taches par le réseaux

		} else {
			// Création de tache bidon pour les tests
			taskList.add(new Tache("Manges des nouilles", "001", "Je suis la tache 1", "Urgent", "En cours", new Date("10/11/2016"),
					new Date("10/01/2016"), "Robert" , "Karl" ));
			taskList.add(new Tache("Manges des patates", "002", "Je suis la tache 2", "Moyenne", "En cours", new Date("12/23/2016"),
					new Date("11/10/2016"), "Claude" , "Jacky" ));
			taskList.add(new Tache("Sucette au coca", "003", "Je suis la tache 3", "Urgent", "Finit", new Date("01/21/2017"),
					new Date("01/01/2017"), "Bebert" , "Momo"));
			taskList.add(new Tache("I am groot", "004", "Je s'appelle Groot !", "Peu Urgent", "Finit", new Date("01/21/2017"),
					new Date("01/01/2017"), "GROOT" , "GROOT"));
		}

		taskList.clear();
		try {
			for (Tache t : taskList) {
				AddTask(t);
			}
		} catch (IOException ex) {
			Logger.getLogger(ListeTachesController.class.getName()).log(Level.SEVERE, null, ex);
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

		// Récupération et application de la date, du mec qui doit bosser/qui a creée et de la priorité
		HBox center_center_top = (HBox) center_center.getTop();
		Label date = (Label) center_center_top.getChildren().get(1);
		Label realisateur = (Label) center_center_top.getChildren().get(4);
		Label priority = (Label) center_center_top.getChildren().get(7);

		date.setText(task.formatter.format(task.dateFin));
		realisateur.setText(task.idRealisateur);
		priority.setText(task.priorite);
		
		//Récupération et application du texte
		TextArea desc = (TextArea) center_center.getCenter();
		desc.setText(task.texte);
		
		// Finition et ajout de la tache
		TitledPane t = new TitledPane(task.tacheID + " : " + task.titre + ". Pour le " + task.formatter.format(task.dateFin), p);
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
	public EventHandler<ActionEvent> ChangeToEditMode = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			Stage stage;
			Button b = (Button) event.getSource();
			TitledPane tp = (TitledPane) b.getParent().getParent().getParent().getParent().getParent();
			
			int i = 0;
			for(Node n : vbox_lstTask.getChildren()){
				if(tp.equals(n))
					break;
				else
					i++;
			}
			TaskEditorHandler.setTacheToEdit(taskList.get(i));
			
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
