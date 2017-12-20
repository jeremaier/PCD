package eu.telecomnancy.pcd2k17;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProjectController implements Initializable {

    @FXML
    private Accordion acc;

    @FXML
    private Label name;

    @FXML
    private Button refresh;

    private GitLabApi gitlab;
    private String token;
    private String user;


    public ProjectController(GitLabApi gla,String privateToken, String name){
        gitlab=gla;
        token=privateToken;
        user=name;
    }


    public void handleNew(ActionEvent event){
        int nb=0;
        try {
            List<Project> list = gitlab.getProjectApi().getMemberProjects();
                for (Project p : list) {
                    if (p.getName().length()>14) {
                        if (p.getName().substring(0, 14).equals("unknownProject")){
                            String proj = p.getName().substring(14, p.getName().length());
                            nb=Integer.max(Integer.parseInt(proj),nb);
                        }
                    }
            }
        } catch (GitLabApiException e) {
            e.printStackTrace();
        }
        nb++;
        nb++;
        String name = "unknownProject" + Integer.toString(nb);
        try {
            Project newProject = gitlab.getProjectApi().createProject(name);
            new ConfigurationView(newProject);
        } catch (GitLabApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void handleRefresh(ActionEvent event){
        Stage stage = (Stage) refresh.getScene().getWindow();
        stage.close();
        new ProjectView(token,user);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        name.setText(user);
        try {
            List<Project> list = gitlab.getProjectApi().getMemberProjects();
            final TitledPane[] tps = new TitledPane[list.size()];
            int i=0;
            for (Project p : list) {
                tps[i]=new TitledPane(p.getName(), setContent(p));
                i++;
            }
            acc.getPanes().addAll(tps);
        } catch (GitLabApiException e) {
            e.printStackTrace();
        }
    }

    public Node setContent(Project p) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ProjectContent.fxml"));
        loader.setControllerFactory(iC-> new ContentController(p,gitlab));
        AnchorPane anchor = null;
        try {
            anchor = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return anchor;
    }

}
