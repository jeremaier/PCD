package eu.telecomnancy.pcd2k17;




import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

import java.io.IOException;

public class MainController {

  @FXML private javafx.scene.control.Button button;

  final static Logger log = LogManager.getLogger(MainController.class);

  @FXML

  public void handleClickOk(ActionEvent event) throws IOException {

    Stage stage = (Stage) button.getScene().getWindow();
    stage.close();

    log.debug("ok button was clicked!");


    new ProjectView("vQZL2K-1161fbxFAwLsS","Quent");


  }

}
