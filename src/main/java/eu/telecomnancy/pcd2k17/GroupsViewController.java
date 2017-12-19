package eu.telecomnancy.pcd2k17;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import org.gitlab4j.api.models.Project;


public class GroupsViewController implements Initializable {

    final static Logger log = LogManager.getLogger(Main.class);

    private int number;
    private int numberGroups;
    private int taille;
    private TextField[][] textes;
    public Group[] groupes;
    public Button[] boutons;

    public Project project;

    @FXML
    private GridPane TextFieldTab;

    @FXML
    private GridPane groupsTab;

    @FXML
    private TextField nameGroupTF;

    @FXML
    private Label groupLabel;

    public GroupsViewController(Project project) {
        this.project = project;
    }

    public void redo() {
        this.groupsTab.getChildren().clear();
        this.groupsTab.setGridLinesVisible(true);
        for (int i=0;i<numberGroups;i++) {
            addGroup(groupes[i],i);
        }
    }


    public void deleteAll() {
        this.numberGroups = 0;
        this.groupes = new Group[100];
        this.groupsTab.getChildren().clear();
    }


    public void removeGroup(int pos) {

        for (int j=pos;j<this.numberGroups;j++) {
            this.groupes[j] = this.groupes[j+1];
        }
        this.groupes[this.numberGroups] = null;
        numberGroups--;
        redo();
        this.groupsTab.setGridLinesVisible(true);
    }
    public void addGroup() {
        Group groupe = new Group(this.numberGroups+1,"Dreamteam",number);
        this.groupes[this.numberGroups] = groupe;
        this.numberGroups++;
        boolean groupOK = true;
        for (int i=0;i<number;i++) {
            if ((textes[i][0].getLength()==0) || (textes[i][1].getLength()==0)) {
                groupOK = false;
                groupLabel.setText("Les noms ne sont pas valables.");
            }
        }


        for (int i=0;i<number;i++) {
            if (groupOK) {
                groupe.addMember(textes[i][0].getText()+" "+textes[i][1].getText());
                groupLabel.setText("Le groupe a été créé !");
                groupLabel.setText("");
            }
        }

        if (nameGroupTF.getText().length()==0) {
            groupe.setName("Groupe "+groupe.id);
        }
        else {
            groupe.setName(nameGroupTF.getText());
        }
        if (groupOK) {
            addGroup(groupe, this.numberGroups);
        }
    }

    private void addGroup(Group groupe, int pos) {
        Label nom = new Label(groupe.name);
        nom.setPadding(new Insets(10, 10, 10, 10));
        //this.numberGroups++;
        nom.setPrefHeight(30);
        groupsTab.add(nom,0,pos);
        for (int n=1;n<groupe.number+1;n++) {
            Label membre = new Label(groupe.getMember(n-1));
            membre.setPadding(new Insets(10, 10, 10, 10));
            membre.setPrefHeight(30);
            groupsTab.add(membre,n,pos);
        }
        boutons[this.numberGroups-1] = new Button("X");
        boutons[this.numberGroups-1].setPadding(new Insets(10, 10, 10, 10));
        int i = this.numberGroups;
        boutons[this.numberGroups-1].setOnAction(e -> removeGroup(pos));
        groupsTab.add(boutons[this.numberGroups-1],groupe.number+1,pos);

        this.taille = (numberGroups+1)*30+(numberGroups)*2;
        groupsTab.setPrefSize(390,taille);

        this.groupsTab.setGridLinesVisible(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.groupes = new Group[100];
        this.number = 4;
        this.numberGroups = 0;
        textes = new TextField[number][2];
        taille = number*30+(number-1)*5;
        TextFieldTab.setHgap(5);
        TextFieldTab.setVgap(5);
        TextFieldTab.setPrefSize(390,taille);
        this.boutons = new Button[100];

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
    }
}