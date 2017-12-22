package eu.telecomnancy.pcd2k17;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Group;
import org.gitlab4j.api.models.Member;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.User;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.geometry.Pos.CENTER;

public class StudentListController implements Initializable {

    public Group groupe;

    public int memberAdded;

    @FXML
    private GridPane studentTab;

    public ArrayList<Button> boutons;

    public ArrayList<MemberInformations> membres;

    public ProjectsController groupes;

    public StudentListController(Group groupe, ProjectsController groupes){
        this.groupe = groupe;
        this.boutons = new ArrayList<Button>();
        this.groupes = groupes;
    }

    public void addStudent(int number) {

        memberAdded = this.groupes.namesAdded.size();
        if ((memberAdded < groupes.number)&&memberAdded>=0) {
            groupes.textes[memberAdded][0].setText(this.membres.get(number-1).getLastName());
            groupes.textes[memberAdded][1].setText(this.membres.get(number-1).getFirstname());

            boutons.get(number-1).setDisable(true);
            this.groupes.namesAdded.add(groupes.textes[memberAdded][0].getText());
            this.groupes.studentsAdded.add(number-1);
            memberAdded++;
        }
        if (memberAdded==groupes.number) {
            for (int i=0;i<this.boutons.size();i++) {
                boutons.get(i).setDisable(true);
            }
        }
        memberAdded = this.groupes.namesAdded.size();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.membres = new ArrayList<MemberInformations>();
        this.membres = GroupConfiguration.getById(groupe.getId()).getMembersList();

        int taille;
        taille = (membres.size()+1)*30+(membres.size())*2;
        studentTab.setPrefSize(480,taille);

        Label ajoute = new Label("Ajouter");
        ajoute.setPadding(new Insets(10, 10, 10, 10));
        ajoute.setPrefHeight(30);
        studentTab.getRowConstraints();

        Label nomEx = new Label("Nom");
        nomEx.setPadding(new Insets(10, 10, 10, 10));
        nomEx.setPrefHeight(30);

        Label prenomEx = new Label("Prénom");
        prenomEx.setPadding(new Insets(10, 10, 10, 10));
        prenomEx.setPrefHeight(30);

        //ajoute.setAlignment(CENTER);
        ajoute.setStyle("-fx-background-color: #DCDCDC;");
        nomEx.setStyle("-fx-background-color: #DCDCDC;");
        prenomEx.setStyle("-fx-background-color: #DCDCDC;");
        studentTab.add(ajoute,0,0);
        studentTab.setHalignment(ajoute, HPos.CENTER);
        studentTab.setStyle("-fx-background-color: #DCDCDC;");
        studentTab.add(nomEx,1,0);
        studentTab.add(prenomEx,2,0);

        for (int i=0;i<membres.size();i++) {

            Pane panneauNom = new Pane();
            Label nom = new Label(membres.get(i).getLastName());
            nom.setPadding(new Insets(10, 10, 10, 10));
            nom.setPrefHeight(30);

            Pane panneauPrenom = new Pane();
            Label prenom = new Label(membres.get(i).getFirstname());
            prenom.setPadding(new Insets(10, 10, 10, 10));
            prenom.setPrefHeight(30);

            Pane panneauAjoute = new FlowPane();


            boutons.add(new Button("+"));
            //boutons.get(i).setPadding(new Insets(-2, -2, -2, -2));
            final int j = i+1;
            boutons.get(i).setOnAction(e -> addStudent(j));

            studentTab.setHalignment(boutons.get(i), HPos.CENTER);
            boutons.get(i).setStyle("-fx-font-size : 10");

            panneauNom.getChildren().add(nom);
            panneauPrenom.getChildren().add(prenom);
            panneauAjoute.getChildren().add(boutons.get(i));
            panneauAjoute.setStyle("-fx-alignment : center;");


            if (i%2==0) {
                panneauNom.setStyle("-fx-background-color: #A9A9A9; -fx-alignment : center;");
                panneauPrenom.setStyle("-fx-background-color: #A9A9A9; -fx-alignment : center;");
                panneauAjoute.setStyle("-fx-background-color: #A9A9A9; -fx-alignment : center;");
            }
            studentTab.add(panneauNom,1,i+1);
            studentTab.add(panneauPrenom,2,i+1);
            studentTab.add(panneauAjoute,0,i+1);
            studentTab.setHalignment(panneauAjoute, HPos.CENTER);

        }

    }

}
