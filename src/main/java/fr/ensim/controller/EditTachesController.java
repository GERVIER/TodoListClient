package fr.ensim.controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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

public class EditTachesController implements Initializable {
	private Tache taskToEdit;
	public SimpleDateFormat formatterForEdit = new SimpleDateFormat("yyyy-MM-dd");
	
	@FXML
	ComboBox<String> lb_priority;
	@FXML
	Button bt_retour;
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
		setTask();
		
		bt_retour.setOnAction(ReturnToList);
	}
	
	private void setTask(){
		taskToEdit = TaskEditorHandler.getTacheToEdit();
		
		lb_priority.getSelectionModel().select(taskToEdit.priorite);
		lb_date.setValue(LocalDate.parse(formatterForEdit.format(taskToEdit.dateFin)));
		lb_desc.setText(taskToEdit.texte);
		lb_titre.setText(taskToEdit.tacheID);
	}
	
    /**
     * 
     */
    public EventHandler<ActionEvent> ReturnToList = new EventHandler<ActionEvent>() {
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
