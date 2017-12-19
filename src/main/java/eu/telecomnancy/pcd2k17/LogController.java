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
    @FXML TextField cToken;
    @FXML Button connect_button;

    String connectiontoken;

    final static Logger log = LogManager.getLogger( LogController.class);



    public void TryConnect(ActionEvent event){
        log.debug("connection button was clicked!");
        if ((identifiant_fill.getText().equals("admin") ) && (password_fill.getText().equals("admin"))){

            connectiontoken=cToken.getText();
            Stage stage = (Stage) connect_button.getScene().getWindow();
            stage.close();

            try {
                new ProjectView(connectiontoken);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            identifiant_fill.clear();
            password_fill.clear();
        }




    }

}
