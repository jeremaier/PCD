package eu.telecomnancy.pcd2k17;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.*;
import org.gitlab4j.api.models.Group;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

import static java.lang.Thread.sleep;

public class ContentController implements Initializable {

    @FXML
    private GridPane table;

    @FXML
    private ScrollPane sp;

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
                mailStage.setTitle("SchoolRoom - Gmail");

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("GmailView.fxml"));
                loader.setControllerFactory(iC-> new GmailSTMP(thisgroup,gitlab));
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

        GroupConfiguration gc = GroupConfiguration.getById(thisgroup.getId());
        int height=0;
        int width=0;
        int nbmembers = gc.getNbMembers();
        System.out.println(nbmembers);
        try {
            List<Project> list = gitlab.getGroupApi().getProjects(thisgroup.getId());
            table.setGridLinesVisible(true);
            table.add(new Label("   Nom du projet   "),0,0);
            table.setMinSize(100,30);
            height=height+30;
            width=width+300;
            for (int k=1;k<=nbmembers;k++){
                Label text = new Label("   Elève "+Integer.toString(k)+"   ");
                text.setMinSize(200,30);
                table.add(text,k,0);
                table.setMinSize(200,30);
                width=width+200;
            }
            Label stats = new Label("  Statistiques  ");
            stats.setMinSize(100,30);
            table.add(stats,nbmembers+1,0);
            table.setMinSize(100,30);
            width=width+100;
            Label contact = new Label("  Contacter  ");
            contact.setMinSize(100,30);
            table.add(contact,nbmembers+2,0);
            table.setMinSize(100,30);
            width=width+100;
            Label supp = new Label("  Supprimer  ");
            supp.setMinSize(100,30);
            table.add(supp,nbmembers+3,0);
            table.setMinSize(100,30);
            width=width+100;
            int i=1;
            for (Project p : list){
                //Récupérer la liste des membres d'un projet
                List<Member> mem = gitlab.getProjectApi().getMembers(p.getId());
                addRow(mem,i,p.getId(),nbmembers,p);
                height=height+30;
                i++;
            }
        } catch (GitLabApiException e) {
            e.printStackTrace();
        }
        table.setPrefHeight(height);
        table.setPrefWidth(width);

        LocalDate date = null;
        if(gc.getLastDay()!=null){
            if ((date.now().isAfter(gc.getFirstDay()) || date.now().isEqual(gc.getFirstDay())) && (date.now().isBefore(gc.getLastDay()) || date.now().isEqual(gc.getLastDay()))){
                statut.setText("En cours.");
            }
            else if (date.now().isBefore(gc.getFirstDay())){
                statut.setText("À venir...");
            }
            else{
                statut.setText("Terminé.");
            }
        }
        else{
            statut.setText("Date non définie");
        }
        Text description = new Text("Identidiant du projet : " + Integer.toString(thisgroup.getId())+"\n"+thisgroup.getDescription());
        text.getChildren().add(description);


    }

    private void addRow(List<Member> members, int nbrow, int id,int nbmembers, Project p) {
        int i=1;
        for (Member m : members){
            Label membre = new Label("   "+m.getName());
            this.table.add(membre, i, nbrow);
            i++;
        }
        Button stats = new Button("          \uD83D\uDDE0          ");
        stats.setOnAction(e -> System.out.println("coucou")); //new GroupstatView(gitlab,id));
        table.add(stats, nbmembers+1, nbrow);

        Button contact = new Button("          ✉          ");
        contact.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage mailStage = new Stage();
                mailStage.setTitle("SchoolRoom - Gmail");

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("GmailView.fxml"));
                loader.setControllerFactory(iC-> new GmailSTMP(thisgroup,gitlab));
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

        table.add(contact, nbmembers+2, nbrow);
        Button supp = new Button("          ✖          ");
        supp.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Supprimer");
            alert.setHeaderText(null);
            alert.setContentText("Êtes-vous sûr(e) de vouloir continuer ?\nPensez à actualiser après suppression.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    gitlab.getProjectApi().deleteProject(p);
                } catch (GitLabApiException e1) {
                    e1.printStackTrace();
                }
            }
        }); //new GroupstatView(gitlab,id));
        table.add(supp, nbmembers+3, nbrow);
    }


}

