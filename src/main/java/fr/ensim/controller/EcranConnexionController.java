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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EcranConnexionController implements Initializable {

	@FXML
	private Button bt_GoToInscri;
	@FXML
	private Button bt_Connexion;
	@FXML
	private Label lb_ServerState;
	@FXML
	private TextField txt_mdp;
	@FXML
	private TextField txt_mail;

	/**
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		bt_GoToInscri.setOnAction(GoToInscri);
		bt_Connexion.setOnAction(TryConnexion);
		txt_mail.setText("johndo@hotmail.fr");
		txt_mdp.setText("123");
		System.out.println("Serveur online : " + networkHandler.isServerOnline());

		if (!networkHandler.isServerOnline()) {
			lb_ServerState.setVisible(true);
		}
	}

	/**
	 * Switch sur la vue d'inscription.
	 */
	public EventHandler<ActionEvent> GoToInscri = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			try {
				Stage stage;

				Button b = (Button) event.getSource();
				stage = (Stage) b.getScene().getWindow();
				switchToView("/fxml/EcranInscription.fxml", stage);
			} catch (IOException ex) {
				ex.getMessage();
			}
		}
	};

	/**
	 * Envoie un message au serveur et si reponse positive, on passe sur la
	 * liste des taches.
	 */
	public EventHandler<ActionEvent> TryConnexion = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			try {
				networkHandler.sendMsgToServ("CONNEXION\n");
				Thread.sleep(3);
				networkHandler.sendMsgToServ(txt_mail.getText() + "\n");
				Thread.sleep(3);
				networkHandler.sendMsgToServ("123" + "\n");
				
				String msg = networkHandler.rcvMsgFromServ();
				System.out.println("Réponse: " + msg);
				
				if(msg.equals("OK") || msg.equals("DEMOMODE")){
					Stage stage;
					Button b = (Button) event.getSource();
					stage = (Stage) b.getScene().getWindow();
					
					switchToView("/fxml/ListeTaches.fxml", stage);
				}else{
					lb_ServerState.setVisible(true);
					lb_ServerState.setText("Login ou mot de passe incorrect");
				}

			} catch (IOException ex) {
				ex.getMessage();
			} catch (InterruptedException e) {
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
