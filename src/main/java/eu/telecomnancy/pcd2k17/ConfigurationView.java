package eu.telecomnancy.pcd2k17;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.models.Group;

import java.io.IOException;

public class ConfigurationView {

    private final static Logger log = LogManager.getLogger(ConfigurationView.class);

    public ConfigurationView(GitLabApi gitLab, Group group, String token, Button refreshButton) throws IOException {
        super();
        Stage configurationStage = new Stage();
        configurationStage.setTitle("Configuration d'un groupe de projets");
        configurationStage.setResizable(false);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("configurationView.fxml"));
        loader.setControllerFactory(iC -> new ConfigurationViewController(gitLab, group, token, refreshButton));
        Parent root = loader.load();

        configurationStage.setOnCloseRequest(event -> {
            log.debug("Fermeture");
            configurationStage.close();
        });

        configurationStage.setScene(new Scene(root, 800, 600));
        configurationStage.show();
    }
}
