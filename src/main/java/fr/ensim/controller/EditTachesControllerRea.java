package fr.ensim.controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Tache;

public class EditTachesControllerRea implements Initializable {
	private Tache taskToEdit;
	private Map<String, String> usersList = ListeTachesController.usersList;
	public SimpleDateFormat formatterForEdit = new SimpleDateFormat("yyyy-MM-dd");

	@FXML
	ComboBox<String> lb_priority;
	@FXML
	ComboBox<String> lb_etat;
	@FXML
	Button bt_retour;
	@FXML
	Button bt_valid;
	@FXML
	DatePicker lb_date;
	@FXML
	TextField lb_who;
	@FXML
	TextArea lb_desc;
	@FXML
	TextField lb_titre;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		lb_priority.getItems().removeAll(lb_priority.getItems());
		lb_priority.getItems().addAll("Peu urgent", "Moyenne", "Urgent");
		
		lb_etat.getItems().removeAll(lb_etat.getItems());
		lb_etat.getItems().addAll("En cours", "Termine", "Bloque");

		setTask();

		bt_retour.setOnAction(ReturnToList);
		bt_valid.setOnAction(ValidTask);
	}

	/**
	 * 
	 */
	private void setTask() {
		taskToEdit = TaskEditorHandler.getTacheToEdit();

		lb_priority.getSelectionModel().select(taskToEdit.priorite);
		lb_etat.getSelectionModel().select(taskToEdit.etat);
		lb_date.setValue(taskToEdit.dateFin);
		lb_desc.setText(taskToEdit.texte);
		lb_titre.setText(taskToEdit.titre);
		lb_who.setText(usersList.get(taskToEdit.idRealisateur));
		
		
	}

	/**
	 *
	 */
	public EventHandler<ActionEvent> ReturnToList = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {

			networkHandler.sendTaskToServ(null, "VALIDATION\n");

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

	/**
	 *
	 */
	public EventHandler<ActionEvent> ValidTask = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {

			Tache taskEdited = new Tache(taskToEdit.titre, taskToEdit.tacheID, taskToEdit.texte,
					taskToEdit.priorite, lb_etat.getValue(), taskToEdit.dateFin, taskToEdit.dateCreation,
					taskToEdit.idCreateur, taskToEdit.idRealisateur);

			networkHandler.sendTaskToServ(taskEdited, "VALIDATION\n");
			
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
