package eu.telecomnancy.pcd2k17;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogView {

    final static Logger log = LogManager.getLogger(LogView.class);

    public LogView (Stage primaryStage) throws Exception{
        primaryStage.setTitle("SchoolRoom login");

        FXMLLoader logviewloader = new FXMLLoader();
        logviewloader.setLocation(getClass().getResource("log.fxml"));
        Parent rootlog = logviewloader.load();

        primaryStage.setOnCloseRequest(event -> {
            log.debug("terminating application.");
            Platform.exit();
        });


        primaryStage.setScene(new Scene(rootlog, 600, 200));
        primaryStage.show();
    }


}

