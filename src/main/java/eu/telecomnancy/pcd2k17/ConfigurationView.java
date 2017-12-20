package eu.telecomnancy.pcd2k17;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gitlab4j.api.models.Project;

import java.io.IOException;

public class ConfigurationView {

    final static Logger log = LogManager.getLogger(ConfigurationView.class);
    private Project project;

    public ConfigurationView(Project project) throws IOException {
        super();
        this.project = project;
        Stage configurationStage = new Stage();
        configurationStage.setTitle("Configuration d'un projet");
        configurationStage.setResizable(false);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("configurationView.fxml"));
        loader.setControllerFactory(iC -> new ConfigurationViewController(project));
        Parent root = loader.load();

        configurationStage.setOnCloseRequest(event -> {
            log.debug("Fermeture");
            configurationStage.close();
        });

        configurationStage.setScene(new Scene(root, 800, 600));
        configurationStage.show();
    }
}

