package eu.telecomnancy.pcd2k17;

import javafx.application.Platform;
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
import org.gitlab4j.api.models.Group;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GroupController implements Initializable {

    @FXML
    private Accordion acc;

    @FXML
    private Label name;

    @FXML
    private Button refresh;

    private GitLabApi gitlab;
    private String token;
    private String user;


    public GroupController(GitLabApi gla,String privateToken, String name){
        gitlab=gla;
        token=privateToken;
        user=name;
    }

    public void handleNew(ActionEvent event){
        int nb=0;
        try {
            List<Group> group = gitlab.getGroupApi().getGroups();
            for (Group g : group) {
                if (g.getName().length()>14) {
                    if (g.getName().substring(0, 14).equals("unknownProject")){
                        String proj = g.getName().substring(14, g.getName().length());
                        nb=Integer.max(Integer.parseInt(proj),nb);
                    }
                }
            }
        } catch (GitLabApiException e) {
            e.printStackTrace();
        }
        nb++;
        String name = "unknownProject" + Integer.toString(nb);
        try {
            gitlab.getGroupApi().addGroup(name,name);
            Group group = gitlab.getGroupApi().getGroup(name);
            new ConfigurationView(gitlab,group);
        } catch (GitLabApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }

    }

    public void handleRefresh(ActionEvent event){
        Stage stage = (Stage) refresh.getScene().getWindow();
        stage.close();
        new GroupView(token,user);
    }

    public void handleQuit(ActionEvent event){
        Platform.exit();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        name.setText(user);
        boolean ok = false;
        try {
            List<Group> groups = gitlab.getGroupApi().getGroups();
            //GroupConfiguration.saveGroupsInFile(getNewGroupsInGit(groups));
            final TitledPane[] tps = new TitledPane[groups.size()];
            int i=0;
            for (Group p : groups) {
                tps[i]=new TitledPane(p.getName(), setContent(p));
                ok=true;
                i++;
            }
            if(ok) acc.getPanes().addAll(tps);
        } catch (GitLabApiException e) {
            e.printStackTrace();
        }
    }

    public Node setContent(Group p) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("GroupContent.fxml"));
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
