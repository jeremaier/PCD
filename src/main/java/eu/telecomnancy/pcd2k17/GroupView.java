package eu.telecomnancy.pcd2k17;


import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gitlab4j.api.GitLabApi;

import java.io.IOException;

public class GroupView {

    protected GitLabApi gla;

    final static Logger log = LogManager.getLogger(GroupView.class);


    public GroupView(String privateToken, String name) {

        gla = new GitLabApi("https://gitlab.telecomnancy.univ-lorraine.fr", privateToken);

        Stage window = new Stage();
        window.setTitle("SchoolRoom - Projects");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("GroupView.fxml"));
        loader.setControllerFactory(iC-> new GroupController(gla,privateToken,name));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            window.close();
        }
        window.setOnCloseRequest(event -> {
            log.debug("terminating application.");
            Platform.exit();
        });
        window.setResizable(false);
        window.setScene(new Scene(root, 1000, 700));
        window.show();
    }

}
