package eu.telecomnancy.pcd2k17;

import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import javafx.application.Application;

import javafx.stage.Stage;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.models.Member;

import java.util.Date;
import java.util.List;


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
      new GroupView("4E_y1qKSCRJzdMNy-49f","Quent");

  }



}
