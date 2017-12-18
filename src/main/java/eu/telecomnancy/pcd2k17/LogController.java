package eu.telecomnancy.pcd2k17;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class LogController {

    @FXML TextField password_fill;
    @FXML TextField identifiant_fill;
    @FXML Button connect_button;


    final static Logger log = LogManager.getLogger( LogController.class);


    public boolean TryConnect(ActionEvent event) {
        log.debug("connection button was clicked!");
        if ((identifiant_fill.getText().equals("admin") ) && (password_fill.getText().equals("admin"))){
            Stage stage = (Stage) connect_button.getScene().getWindow();
            stage.close();
            return true;
        } else {
            identifiant_fill.clear();
            password_fill.clear();
            return false;
        }




    }

}
