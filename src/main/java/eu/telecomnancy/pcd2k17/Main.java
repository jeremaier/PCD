package eu.telecomnancy.pcd2k17;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import javafx.application.Application;

import javafx.stage.Stage;
import org.gitlab4j.api.GitLabApi;

import java.util.Date;


public class Main extends Application  {


  //vQZL2K-1161fbxFAwLsS


  final static Logger log = LogManager.getLogger(Main.class);

  public static void main(String args[]) {
    log.debug("executing main() method.");
    launch(args);
  }

  public GitLabApi gla;

  @Override
  public void start(Stage primaryStage) throws Exception {

      //new LogView(primaryStage);

      gla = new GitLabApi("https://gitlab.telecomnancy.univ-lorraine.fr", "shcz2dxdWdAFw5dc7q6-");
      new GroupstatView(gla,1274 );
      //new ProjectView("vQZL2K-1161fbxFAwLsS","Quent");


  }



}
