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
            networkHandler.sendMsgToServ("INSCRIPTION\n" + txt_Mail.getText() + "\n" + txt_name.getText());
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
     * @param view La nouvelle vue voulu
     * @param stage La fenetre pour la quelle la vue est à changer
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
