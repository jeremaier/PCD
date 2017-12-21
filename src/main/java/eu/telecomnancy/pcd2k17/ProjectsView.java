package eu.telecomnancy.pcd2k17;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.models.Group;
import org.gitlab4j.api.models.Project;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
//import Group;

public class ProjectsView {

    final static Logger log = LogManager.getLogger(Main.class);

    public ProjectsView(Stage primaryStage, Group groupe, GitLabApi gitlab) throws Exception{

        primaryStage.setTitle("SchoolRoom groups");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ProjectsView.fxml"));
        ProjectsController projet = new ProjectsController(groupe, gitlab);
        loader.setControllerFactory(iC -> projet);
        Parent root = loader.load();

        primaryStage.setOnCloseRequest(event -> {
            log.debug("terminating application.");
            primaryStage.close();
            projet.liste.stage.close();
        });


        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

        Rectangle2D coord = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(coord.getWidth()*1/8);
        primaryStage.setY(50);

    }
}
