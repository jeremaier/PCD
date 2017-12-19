package eu.telecomnancy.pcd2k17;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;

public class ConfigurationView {

    final static Logger log = LogManager.getLogger(ConfigurationView.class);
    private static Project project;

    public ConfigurationView(){}

    public ConfigurationView(Project proj) throws IOException {
        log.debug("executing ConfigurationView method.");
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

    public static Project getProject() {
        return project;
    }

    public static void setProject(String title, String module, String nbMembers, LocalDate firstDate,LocalDate lastDate, String description) {
        project = new Project(title, module, nbMembers, firstDate, lastDate, description);
    }
}

