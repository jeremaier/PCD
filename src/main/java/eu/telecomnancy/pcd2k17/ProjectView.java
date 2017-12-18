package eu.telecomnancy.pcd2k17;


import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;

import java.io.IOException;
import java.util.List;

public class ProjectView {


    public ProjectView() {
        GitLabApi gitLab = new GitLabApi("https://gitlab.telecomnancy.univ-lorraine.fr", "vQZL2K-1161fbxFAwLsS");
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
