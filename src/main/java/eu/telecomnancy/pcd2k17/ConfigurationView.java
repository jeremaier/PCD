package eu.telecomnancy.pcd2k17;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigurationView extends Application {
    final static Logger log = LogManager.getLogger(ConfigurationView.class);

    public void startConfigurationView() {
        log.debug("executing ConfigurationView method.");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("JFX Sample Application");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("configurationView.fxml"));
        Parent root = loader.load();

        primaryStage.setOnCloseRequest(event -> {
            log.debug("terminating application.");
            Platform.exit();
        });

        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.show();
    }
}
