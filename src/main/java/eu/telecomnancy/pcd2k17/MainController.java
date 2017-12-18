package eu.telecomnancy.pcd2k17;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
  public void handleClickOk(ActionEvent event) {
    Stage stage = (Stage) button.getScene().getWindow();
    stage.close();

    log.debug("ok button was clicked!");

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

  }

}
