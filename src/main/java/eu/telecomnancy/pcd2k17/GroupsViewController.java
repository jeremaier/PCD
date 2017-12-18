package eu.telecomnancy.pcd2k17;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ScrollPane;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.control.Separator;
import javafx.geometry.Insets;

public class GroupsViewController implements Initializable {

    final static Logger log = LogManager.getLogger(Main.class);

    public int number;
    public int numberGroups;
    public int taille;
    public TextField[][] textes;
    public Group[] groupes;

    @FXML
    private GridPane TextFieldTab;

    @FXML
    private GridPane groupsTab;

    public void addGroup(Group groupe) {
        Label nom = new Label(groupe.name);
        nom.setPadding(new Insets(10, 10, 10, 10));
        this.numberGroups++;
        nom.setPrefHeight(30);
        groupsTab.add(nom,0,this.numberGroups);
        for (int n=1;n<groupe.number+1;n++) {
            Label membre = new Label(groupe.getMember(n-1));
            membre.setPadding(new Insets(10, 10, 10, 10));
            membre.setPrefHeight(30);
            groupsTab.add(membre,n,this.numberGroups);
        }
        this.taille = (numberGroups+1)*30+(numberGroups)*2;
        groupsTab.setPrefSize(390,taille);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.number = 4;
        textes = new TextField[number][2];
        taille = number*30+(number-1)*5;
        TextFieldTab.setHgap(5);
        TextFieldTab.setVgap(5);
        TextFieldTab.setPrefSize(390,taille);

        for (int i=0;i<number;i++) {

            TextField text1 = new TextField();
            text1.setPromptText("Nom");
            text1.setPrefHeight(30);

            TextField text2 = new TextField();
            text2.setPromptText("Prénom");
            text2.setPrefHeight(30);

            textes[i][0] = text1;
            TextFieldTab.add(text1,0,i);
            textes[i][1] = text2;
            TextFieldTab.add(text2,1,i);
        }


        this.numberGroups = 0;
        groupes = new Group[numberGroups];
        Group groupetest = new Group(1,"Dreamteam",number);
        groupetest.addMember("Maxence Lefort");
        groupetest.addMember("Jérémy Sernit");
        groupetest.addMember("Matta Kar");
        groupetest.addMember("Quentin Lefeuvre");

        this.taille = (numberGroups+1)*30+(numberGroups)*2;
        groupsTab.setPrefSize(390,taille);
        //groupsTab.setHgap(10);
        //groupsTab.setVgap(5);


        Label nom = new Label("Nom du groupe");
        nom.setPadding(new Insets(10, 10, 10, 10));
        nom.setPrefHeight(30);
        groupsTab.add(nom,0,0);
        for (int n=1;n<number+1;n++) {
            Label membre = new Label("Membre "+n);
            membre.setPadding(new Insets(10, 10, 10, 10));
            membre.setPrefHeight(30);
            groupsTab.add(membre,n,0);
        }
        this.addGroup(groupetest);

        /*for (int j=0;j<numberGroups;j++) {
            Group groupe = new Group(j,"Dreamteam",number);
            for (int k=0;k<number;k++) {

            }

            groupes[j] = groupe;
            Label nom = new Label(groupe.name);
            nom.setPrefHeight(30);
            groupsTab.add(nom,0,j);
            Label membres = new Label(groupe.getMembers());
            membres.setPrefHeight(30);
            groupsTab.add(membres,1,j);
        }*/

    }
}
