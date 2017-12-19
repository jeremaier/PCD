package eu.telecomnancy.pcd2k17;

import javafx.scene.Node;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
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
    //private int this.groupes2.size();
    private int taille;
    private TextField[][] textes;
    public Group[] groupes;
    public ArrayList<Group> groupes2;
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

    @FXML
    private Button studentListButton;

    public GroupsViewController(Project project) {
        this.project = project;
    }

    public void redo() {
        Node node = this.groupsTab.getChildren().get(0);
        this.groupsTab.getChildren().clear();
        this.groupsTab.getChildren().add(0,node);
        //this.groupsTab.setGridLinesVisible(true);
        for (int i=0;i<this.groupes2.size();i++) {
            addGroup(groupes2.get(i),i+1);
        }
    }

    public void deleteAll() {
        //this.groupes2.size() = 0;
        this.groupes = new Group[100];
        Node node = this.groupsTab.getChildren().get(0);
        this.groupsTab.getChildren().clear();
        this.groupsTab.getChildren().add(0,node);
    }


    public void removeGroup(int pos) {
        //pos = pos-1;
        /*for (int j=pos;j<this.groupes2.size();j++) {
            this.groupes[j] = this.groupes[j+1];
        }
        this.groupes[this.groupes2.size()] = null;*/

        this.groupes2.remove(groupes2.get(pos));
        redo();
    }
    public void addGroup() {
        Group groupe = new Group(this.groupes2.size()+1,"Dreamteam",number);
        //this.groupes[this.groupes2.size()+1] = groupe;
        this.groupes2.add(groupe);
        //this.groupes2.size()++;
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
                textes[i][0].setText("");
                textes[i][0].setPromptText("Nom");
                textes[i][1].setText("");
                textes[i][1].setPromptText("Prénom");
            }
        }

        if (nameGroupTF.getText().length()==0) {
            groupe.setName("Groupe "+groupe.id);
        }
        else {
            groupe.setName(nameGroupTF.getText());
        }
        if (groupOK) {
            groupLabel.setText("");
            nameGroupTF.setText("");
            nameGroupTF.setPromptText("Nom du groupe");
            addGroup(groupe, this.groupes2.size());
        }
    }

    private void addGroup(Group groupe, int pos) {
        Label nom = new Label(groupe.name);
        nom.setPadding(new Insets(10, 10, 10, 10));
        //this.groupes2.size()++;
        nom.setPrefHeight(30);
        groupsTab.add(nom,0,pos);
        for (int n=1;n<groupe.number+1;n++) {
            Label membre = new Label(groupe.getMember(n-1));
            membre.setPadding(new Insets(10, 10, 10, 10));
            membre.setPrefHeight(30);
            groupsTab.add(membre,n,pos);
        }
        boutons[this.groupes2.size()-1] = new Button("X");
        boutons[this.groupes2.size()-1].setPadding(new Insets(10, 10, 10, 10));
        //int i = this.groupes2.size();
        boutons[this.groupes2.size()-1].setOnAction(e -> removeGroup(pos-1));
        groupsTab.add(boutons[this.groupes2.size()-1],groupe.number+1,pos);

        this.taille = (this.groupes2.size()+1)*30+(this.groupes2.size())*2;
        groupsTab.setPrefSize(390,taille);
    }

    public void showStudentList() {
        try {
            new StudentListView(this.project);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /*try {
            new StudentListView(this.project);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        //this.groupes = new Group[100];
        this.groupes2 = new ArrayList<Group>();
        this.number = 4;
        //this.groupes2.size() = 0;
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