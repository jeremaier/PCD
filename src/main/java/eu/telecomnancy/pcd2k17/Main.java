package eu.telecomnancy.pcd2k17;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application  {

  final static Logger log = LogManager.getLogger(Main.class);

  public static void main(String args[]) {
    log.debug("executing main() method.");
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    new LogView(primaryStage);

  }



}
