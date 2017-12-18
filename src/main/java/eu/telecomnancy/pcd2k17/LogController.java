package eu.telecomnancy.pcd2k17;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

import java.io.IOException;

public class LogController {

    @FXML TextField password_fill;
    @FXML TextField identifiant_fill;
    @FXML Button connect_button;


    final static Logger log = LogManager.getLogger( LogController.class);


    public void TryConnect(ActionEvent event) {
        log.debug("connection button was clicked!");
        if ((identifiant_fill.getText().equals("admin") ) && (password_fill.getText().equals("admin"))){
            Stage stage = (Stage) connect_button.getScene().getWindow();
            stage.close();

            Stage window = new Stage();
            window.setTitle("SchoolRoom - Project");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ProjectView.fxml"));
            loader.setControllerFactory(iC-> new ProjectView());
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            window.setOnCloseRequest(event2 -> {
                log.debug("terminating application.");
                Platform.exit();
            });

            window.setScene(new Scene(root, 800, 600));
            window.show();
        } else {
            identifiant_fill.clear();
            password_fill.clear();
        }




    }

}
