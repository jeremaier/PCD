package eu.telecomnancy.pcd2k17;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;

import java.io.IOException;
import java.util.List;

public class ProjectView {

    @FXML
    TitledPane t1;
    @FXML
    Accordion accordion= new Accordion();


    final static Logger log = LogManager.getLogger(ProjectView.class);

    public ProjectView(String privateToken) throws IOException {

        GitLabApi gitLab = new GitLabApi("https://gitlab.telecomnancy.univ-lorraine.fr", privateToken);

        try {
            List<Project> list = gitLab.getProjectApi().getMemberProjects();
            final TitledPane[] tps = new TitledPane[list.size()];
            final Button[] buttons =new Button[list.size()];
            int i=0;
            System.out.println("COUCOU");
            System.out.println(list.size());
            for (Project p : list) {
                buttons[i]=new Button("Modifier");
                buttons[i].setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            new ConfigurationView(p);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                tps[i]=new TitledPane(p.getName(), buttons[i]);
                System.out.println(p.getName());
                System.out.println(p.getArchived());
                i++;
            }
            accordion.getPanes().addAll(tps);
            accordion.setExpandedPane(tps[0]);
        } catch (GitLabApiException e) {
            e.printStackTrace();
        }

        Stage window = new Stage();
        window.setTitle("SchoolRoom - Project");
        Scene scene = new Scene(new Group(), 800, 600);

        /*
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ProjectView.fxml"));

        AnchorPane root = loader.load();
        */

        window.setOnCloseRequest(event2 -> {
            log.debug("terminating application.");
            Platform.exit();
        });

        //root.getChildren().add(accordion);

        Group root = (Group)scene.getRoot();
        root.getChildren().add(accordion);
        window.setScene(scene);
        //window.setScene(new Scene(root, 800, 600));
        window.show();

    }

}
