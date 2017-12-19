package eu.telecomnancy.pcd2k17;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class MainController {

  final static Logger log = LogManager.getLogger(MainController.class);

  @FXML
  public void handleClickOk(ActionEvent event) {
    log.debug("ok button was clicked!");
  }

}
