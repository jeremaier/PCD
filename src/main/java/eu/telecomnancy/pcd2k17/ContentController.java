package eu.telecomnancy.pcd2k17;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
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
                thisgroup.setDescription("Swag total");
                thisgroup.setName("Askip");
                thisgroup.setPath("Askip");
                updateThisGroup();
                try {
                    new ConfigurationView(gitlab,thisgroup);
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
                    //new GroupsView(groupStage,thisgroup);
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

    public void updateThisGroup(){
        try {
            thisgroup = gitlab.getGroupApi().updateGroup(thisgroup.getId(),thisgroup.getName(),thisgroup.getPath(),thisgroup.getDescription(),thisgroup.getRequestAccessEnabled(),thisgroup.getRequestAccessEnabled(),thisgroup.getVisibility(),thisgroup.getRequestAccessEnabled(),thisgroup.getRequestAccessEnabled(),thisgroup.getParentId(),thisgroup.getSharedRunnersMinutesLimit());
        } catch (GitLabApiException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Requête impossible");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur est survenue durant la mise à jour des données.\n\nVérifiez que le nom n'est pas déjà utilisé.");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }
}
