package eu.telecomnancy.pcd2k17;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GroupstatView {

    final static Logger log = LogManager.getLogger(GroupstatView.class);

    public GroupstatView () throws Exception{

        Stage window = new Stage();

        window.setTitle("SchoolRoom login");

        FXMLLoader logviewloader = new FXMLLoader();
        logviewloader.setLocation(getClass().getResource("log.fxml"));
        Parent rootlog = logviewloader.load();

        window.setOnCloseRequest(event -> {
            log.debug("terminating application.");
            Platform.exit();
        });


        window.setScene(new Scene(rootlog, 600, 200));
        window.show();

    }


}