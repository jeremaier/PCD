package eu.telecomnancy.pcd2k17;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gitlab4j.api.models.Project;

import java.io.IOException;

public class ConfigurationView{

    final static Logger log = LogManager.getLogger(ConfigurationView.class);

    public ConfigurationView(){

    }


    public ConfigurationView(Project proj) throws IOException {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("JFX Sample Application");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("configurationView.fxml"));
        Parent root = loader.load();

        primaryStage.setOnCloseRequest(event -> {
            log.debug("terminating application.");
            primaryStage.close();
        });

        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }
}
