package eu.telecomnancy.pcd2k17;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Member;
import org.gitlab4j.api.models.Project;

import java.io.IOException;
import java.util.List;


// besoin de public "List<Commit> getCommits(int projectId)"

public class GroupstatView {

    @FXML
    Accordion accordion= new Accordion();
    SplitPane splitpane= new SplitPane();

    final static Logger log = LogManager.getLogger(GroupstatView.class);

    public GroupstatView (GitLabApi gitLab,int groupnb) throws Exception{

        try {

            List<Member> list = gitLab.getGroupApi().getMembers(groupnb);
            final TitledPane[] tps = new TitledPane[list.size()];
            final GridPane[] grid =new GridPane[list.size()];
            final GridPane gridglob= new GridPane();
            int i=0;
            for (Member p : list) {
                grid[i]=new GridPane();
                Label label1= new Label("Date du dernier commit");
                GridPane.setConstraints(label1,0,0);
                Label label2= new Label("Nombre de commits realisé ");
                GridPane.setConstraints(label2,0,1);
                TextFlow tf1= new TextFlow();
                GridPane.setConstraints(tf1,1,0);
                TextFlow tf2= new TextFlow();
                GridPane.setConstraints(tf2,1,1);
                grid[i].getChildren().addAll(label1,label2,tf1,tf2);
                tps[i]=new TitledPane(p.getName(), grid[i]);
                i++;
            }

            Label label1glob= new Label("Date du dernier commit du groupe");
            Label label2glob= new Label("nombre de commits realisés par le groupe");
            TextFlow tf1glob= new TextFlow();
            TextFlow tf2glob= new TextFlow();
            TextFlow nomgroupe = new TextFlow();

            GridPane.setConstraints(nomgroupe,0,0);
            GridPane.setConstraints(label1glob,0,1);
            GridPane.setConstraints(label2glob,0,2);
            GridPane.setConstraints(tf1glob,2,1);
            GridPane.setConstraints(tf2glob,1,2);

            gridglob.getChildren().addAll(label1glob,label2glob,tf1glob,tf2glob);

            accordion.getPanes().addAll(tps);
            accordion.setExpandedPane(tps[0]);
            splitpane.getItems().add(gridglob);
            splitpane.getItems().add(accordion);

            } catch (GitLabApiException e) {
                e.printStackTrace();
            }



        Stage window = new Stage();
        window.setTitle("SchoolRoom - Project");
        Scene scene = new Scene(new Group(), 800, 600);


        window.setOnCloseRequest(event -> {
            log.debug("terminating application.");
            Platform.exit();
        });



        javafx.scene.Group root = (Group)scene.getRoot();
        root.getChildren().add(accordion);
        window.setScene(scene);
        //window.setScene(new Scene(root, 800, 600));
        window.show();

    }


}