package dev.cinomod.MarkdownGenerator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.net.URL;

public class MainView extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        // File requires absolute path. Get current directory then append path to the fxml file
        String fxmlLocation = System.getProperty("user.dir") + "/application/src/main/java/dev/cinomod/MarkdownGenerator/StartView.fxml";
        loader.setLocation(new URL("file://" + fxmlLocation));

        VBox vbox = loader.load();

        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
