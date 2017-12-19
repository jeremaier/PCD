package eu.telecomnancy.pcd2k17;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.fxml.Initializable;
//import Group;

public class GroupsView {

    final static Logger log = LogManager.getLogger(Main.class);

    public GroupsView() {

    }

    public GroupsView (Stage primaryStage) throws Exception{

        primaryStage.setTitle("SchoolRoom groups");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("GroupsView.fxml"));
        Parent root = loader.load();

        primaryStage.setOnCloseRequest(event -> {
            log.debug("terminating application.");
            Platform.exit();
        });


        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

    }
}
