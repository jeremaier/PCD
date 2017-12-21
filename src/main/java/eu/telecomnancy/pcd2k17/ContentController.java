package eu.telecomnancy.pcd2k17;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Group;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class ContentController implements Initializable {

    @FXML
    private Group thisgroup;

    @FXML
    private Label statut;

    @FXML
    private GitLabApi gitlab;

    @FXML
    private Button modify;

    @FXML
    private Button group;

    @FXML
    private Button mail;

    @FXML
    private Button supp;

    @FXML
    private TextFlow text;


    public ContentController(Group g, GitLabApi gla){
        thisgroup=g;
        gitlab=gla;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        modify.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    new ConfigurationView(gitlab,thisgroup);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mail.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage mailStage = new Stage();
                mailStage.setTitle("SchoolRoom groups");

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("GmailView.fxml"));
                loader.setControllerFactory(iC-> new GmailSTMP(thisgroup));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mailStage.setOnCloseRequest(event2 -> {
                    mailStage.close();
                });

                mailStage.setScene(new Scene(root, 600, 400));
                mailStage.show();
            }
        });

        supp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Supprimer");
                alert.setHeaderText(null);
                alert.setContentText("Êtes-vous sûr(e) de vouloir continuer ?\nPensez à actualiser après suppression.");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        gitlab.getGroupApi().deleteGroup(thisgroup);
                    } catch (GitLabApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        group.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage groupStage= new Stage();
                try {
                    //new ProjectsView(groupStage,thisgroup);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Text description = new Text("Identidiant du projet : " + Integer.toString(thisgroup.getId())+"\n"+thisgroup.getDescription());
        text.getChildren().add(description);

        /*
        if (thisgroup.isArchived()){
            statut.setText("Archivé");
        }
        else{
            statut.setText("En cours");
        }
        */
    }

}

