package eu.telecomnancy.pcd2k17;


import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;

import java.io.IOException;
import java.util.List;

public class ProjectView {

    final static Logger log = LogManager.getLogger( ProjectView.class);

    public ProjectView(String connectiontoken) {

        Stage window = new Stage();
        window.setTitle("SchoolRoom - Project");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ProjectView.fxml"));
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

        GitLabApi gitLab = new GitLabApi("https://gitlab.telecomnancy.univ-lorraine.fr", connectiontoken);

        try {
            List<Project> list = gitLab.getProjectApi().getMemberProjects();
            for (Project p : list) {
                System.out.println(p.getName());
                System.out.println(p.getArchived());
            }
        } catch (GitLabApiException e) {
            e.printStackTrace();
        }

    }
}
