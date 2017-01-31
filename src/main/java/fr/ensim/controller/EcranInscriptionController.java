/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensim.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;

/**
 * FXML Controller class
 *
 * @author Rémy
 */
public class EcranInscriptionController implements Initializable {

	@FXML
	TextField txt_nickName;
	@FXML
	TextField txt_name;
	@FXML
	TextField txt_Mail;
	@FXML
	TextField txt_pass;
	@FXML
	TextField txt_passConf;
	@FXML
	Button Bt_valideInscri;
	@FXML
	Button bt_return;

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
		Bt_valideInscri.setOnAction(Try_Inscription);
		bt_return.setOnAction(GoToCo);

	}

	/**
	 *
	 */
	public EventHandler<ActionEvent> Try_Inscription = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			boolean canSend = true;
			if (txt_name.getText().equals("")) {
				txt_name.getStyleClass().add("text-field-error");
				canSend = false;
			}
			if (txt_nickName.getText().equals("")) {
				txt_nickName.getStyleClass().add("text-field-error");
				canSend = false;
			}
			if (txt_Mail.getText().equals("")) {
				txt_Mail.getStyleClass().add("text-field-error");
				canSend = false;
			}
			if (txt_pass.getText().equals("")) {
				txt_pass.getStyleClass().add("text-field-error");
				canSend = false;
			}
			if (txt_passConf.getText().equals("")) {
				txt_passConf.getStyleClass().add("text-field-error");
				canSend = false;
			}
			if (!txt_passConf.getText().equals(txt_pass.getText())) {
				txt_passConf.getStyleClass().add("text-field-error");
				txt_pass.getStyleClass().add("text-field-error");
				canSend = false;
			}
			if (canSend) {
				User u = new User("", txt_nickName.getText(), txt_name.getText(), txt_Mail.getText(),
						txt_pass.getText());
				networkHandler.sendMsgToServ("INSCRIPTION\n");
				networkHandler.sendUserToServ(u);

				Stage stage;
				Button b = (Button) event.getSource();
				stage = (Stage) b.getScene().getWindow();
				try {
					switchToView("/fxml/ListeTaches.fxml", stage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	};

	/**
	 *
	 */
	public EventHandler<ActionEvent> GoToCo = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			try {
				Stage stage;
				Button b = (Button) event.getSource();
				stage = (Stage) b.getScene().getWindow();

				switchToView("/fxml/EcranConnexion.fxml", stage);

			} catch (IOException ex) {
				ex.getMessage();
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
