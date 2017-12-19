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
import org.gitlab4j.api.models.Project;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class ContentController implements Initializable {

    @FXML
    private Project proj;

    @FXML
    private Label statut;

    @FXML
    private GitLabApi gitlab;

    @FXML
    private Button modify;

    @FXML
    private Button supp;

    @FXML
    private TextFlow text;


    public ContentController(Project p, GitLabApi gla){
        proj=p;
        gitlab=gla;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        modify.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    new ConfigurationView(proj);
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
                        gitlab.getProjectApi().deleteProject(proj);
                    } catch (GitLabApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Text description = new Text(proj.getDescription());
        text.getChildren().add(description);

        if (proj.getArchived()){
            statut.setText("Archivé");
        }
        else{
            statut.setText("En cours");
        }
    }
}
