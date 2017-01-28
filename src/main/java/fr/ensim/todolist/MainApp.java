package fr.ensim.todolist;

import fr.ensim.controller.networkHandler;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class MainApp extends Application {
	
	
    @Override
    public void start(Stage stage) throws Exception {
        networkHandler.init();
        stage.onCloseRequestProperty().set(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                networkHandler.sendMsgToServ("END");
            }
        });

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/EcranConnexion.fxml"));
        Scene scene = new Scene(root);
        
        stage.setTitle("TODO List Client");
        stage.setMinWidth(1024);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
