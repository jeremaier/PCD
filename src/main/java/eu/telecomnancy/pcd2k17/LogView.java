package eu.telecomnancy.pcd2k17;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class LogView {

    @FXML
    ProgressIndicator pi;

    final static Logger log = LogManager.getLogger(LogView.class);

    public LogView (Stage primaryStage){

        primaryStage.setTitle("SchoolRoom login");

        FXMLLoader logviewloader = new FXMLLoader();
        logviewloader.setLocation(getClass().getResource("log.fxml"));
        Parent rootlog = null;
        try {
            rootlog = logviewloader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.setOnCloseRequest(event -> {
            log.debug("terminating application.");
            Platform.exit();
        });


        primaryStage.setScene(new Scene(rootlog, 600, 300));
        primaryStage.show();

    }


}

