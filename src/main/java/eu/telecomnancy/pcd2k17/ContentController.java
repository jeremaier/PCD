package eu.telecomnancy.pcd2k17;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.*;
import org.gitlab4j.api.models.Group;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class ContentController implements Initializable {

    @FXML
    private GridPane table;

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

    private GroupConfiguration groupconfig;

    private String privateToken;


    public ContentController(Group g, GitLabApi gla,String token){
        thisgroup=g;
        gitlab=gla;
        privateToken=token;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        GroupConfiguration gc = GroupConfiguration.getById(thisgroup.getId());
        groupconfig=gc;

        modify.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    new ConfigurationView(gitlab,thisgroup,privateToken,modify);
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
                alert.setTitle("Supprimer le groupe");
                alert.setHeaderText(null);
                alert.setContentText("Êtes-vous sûr(e) de vouloir continuer ?\n\nLa fenêtre va se recharger.");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        gitlab.getGroupApi().deleteGroup(thisgroup);
                        Stage stage = (Stage) supp.getScene().getWindow();
                        stage.close();
                        new GroupView(privateToken, gitlab.getUserApi().getCurrentUser().getName());
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
                if(groupconfig.getNbMembers()!=0){
                    try {
                        new ProjectsView(groupStage,thisgroup,gitlab);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Aucun élève !");
                    alert.setHeaderText(null);
                    alert.setContentText("Il n'y a aucun membre à affecter.\n\nVeuillez configurer ce groupe.");
                    alert.showAndWait();
                }

            }
        });

        viewProjects();

    }





    private void addRow(List<Member> members, int nbrow, int id,int nbmembers, Project p) {

        ArrayList<MemberInformations> groupmember = groupconfig.getMembersList();
        int i = 1;
        ArrayList<String> email = new ArrayList<>();
        Label groupname = new Label("   " + p.getName());
        groupname.setFont(new Font("Arial", 15));
        table.add(groupname, 0, nbrow);
        for (Member m : members) {
            Label membre = new Label("   " + m.getName());
            for (MemberInformations mi : groupmember) {
                if (m.getName().toLowerCase().contains((mi.getFirstname().toLowerCase())) && m.getName().toLowerCase().contains((mi.getLastName().toLowerCase()))) {
                    email.add(mi.getEmail());
                }
            }
            this.table.add(membre, i, nbrow);
            i++;
        }


        Button stats = new Button("       ⋰       ");
        stats.setOnAction(e -> {
            try {
                new GroupstatView(gitlab, p.getId(), groupconfig);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        table.add(stats, nbmembers + 1, nbrow);


        Button contact = new Button("       ✉       ");
        contact.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Stage mailStage = new Stage();
                mailStage.setTitle("SchoolRoom - Gmail");

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("GmailView.fxml"));
                loader.setControllerFactory(iC -> new GmailSTMP(email, thisgroup));
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
        table.add(contact, nbmembers + 2, nbrow);

        Button wentto = new Button("    ⟶    ");
        wentto.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI("https://gitlab.telecomnancy.univ-lorraine.fr/" + thisgroup.getFullPath() + "/" + p.getPath()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });
        table.add(wentto, nbmembers + 3, nbrow);

        Button supp = new Button("      ✖      ");
        supp.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Supprimer le projet");
            alert.setHeaderText(null);
            alert.setContentText("Êtes-vous sûr(e) de vouloir continuer ?\n\nLa fenêtre va se recharger.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    gitlab.getProjectApi().deleteProject(p);
                    Stage stage = (Stage) supp.getScene().getWindow();
                    stage.close();
                    new GroupView(privateToken, gitlab.getUserApi().getCurrentUser().getName());
                } catch (GitLabApiException e1) {
                    e1.printStackTrace();
                }
            }
        });
        table.add(supp, nbmembers + 4, nbrow);

    }




    public void viewProjects(){
        int height=0;
        int width=0;
        int nbmembers = groupconfig.getNbMembers();

        try {
            List<Project> list = gitlab.getGroupApi().getProjects(thisgroup.getId());
            Label name= new Label("   Nom du projet   ");
            name.setFont(new Font("Arial",20));
            name.setMinSize(150,30);
            table.add(name,0,0);
            height=height+30;
            width=width+200;

            for (int k=1;k<=nbmembers;k++){
                Label text = new Label("   Elève "+Integer.toString(k)+"   ");
                text.setMinSize(150,30);
                text.setFont(new Font("Arial",20));
                table.add(text,k,0);
                table.setMinSize(150,30);
                width=width+200;
            }

            Label stats = new Label("  Statistiques");
            stats.setMinSize(125,30);
            stats.setFont(new Font("Arial",20));
            table.add(stats,nbmembers+1,0);
            width=width+125;
            Label contact = new Label("  Contacter");
            contact.setMinSize(122,30);
            contact.setFont(new Font("Arial",20));
            table.add(contact,nbmembers+2,0);
            width=width+120;
            Label wento = new Label("   Aller à");
            wento.setFont(new Font("Arial",20));
            wento.setMinSize(102,20);
            table.add(wento,nbmembers+3,0);
            width=width+100;
            Label supp = new Label(" Supprimer");
            supp.setMinSize(130,30);
            supp.setFont(new Font("Arial",20));
            table.add(supp,nbmembers+4,0);
            width=width+130;
            int i=1;
            for (Project p : list){
                //Récupérer la liste des membres d'un projet
                List<Member> mem = gitlab.getProjectApi().getMembers(p.getId());
                addRow(mem,i,p.getId(),nbmembers,p);
                height=height+32;
                i++;
            }
        } catch (GitLabApiException e) {
            e.printStackTrace();
        }
        table.setPrefHeight(height);
        table.setPrefWidth(width);

        LocalDate date = null;
        if(groupconfig.getLastDay()!=null){
            if ((date.now().isAfter(groupconfig.getFirstDay()) || date.now().isEqual(groupconfig.getFirstDay())) && (date.now().isBefore(groupconfig.getLastDay()) || date.now().isEqual(groupconfig.getLastDay()))){
                statut.setText("En cours.");
            }
            else if (date.now().isBefore(groupconfig.getFirstDay())){
                statut.setText("À venir...");
            }
            else{
                statut.setText("Terminé.");
            }
        }
        else{
            statut.setText("Date non définie.");
        }
        Text description = new Text(thisgroup.getDescription());
        text.getChildren().add(description);
    }

}

