package eu.telecomnancy.pcd2k17;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class Main extends Application {

  final static Logger log = LogManager.getLogger(Main.class);

  public static void main(String args[]) {
    log.debug("executing main() method.");
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("TELECOM Nancy Schoolroom");

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
