package eu.telecomnancy.pcd2k17;

import javafx.scene.Node;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.*;


public class ProjectsController implements Initializable {

    final static Logger log = LogManager.getLogger(Main.class);

    public int number;
    //private int this.groupes2.size();
    private int taille;
    public TextField[][] textes;
    public ArrayList<String> namesAdded;
    //public Group[] groupes;
    public ArrayList<ProjectGroups> groupes2;
    public Button[] boutons;
    public StudentListView liste;
    public Group groupe;
    public ArrayList<Button> removeButtons;
    public GitLabApi gitlab;

    public List<User> membersLoaded;

    @FXML
    private GridPane TextFieldTab;

    @FXML
    private GridPane groupsTab;

    @FXML
    private TextField nameGroupTF;

    @FXML
    private Label groupLabel;

    @FXML
    public Button studentListButton;

    @FXML
    public Button validateGroups;

    @FXML
    private boolean studentButton;

    public ProjectsController(Group groupe, GitLabApi gitlab) {

        this.groupe = groupe;
        this.gitlab = gitlab;
    }


    public ArrayList<MemberInformations> compare(List<User> membres, ArrayList<MemberInformations> membresInfo) {
        ArrayList<MemberInformations> resultat;
        resultat = membresInfo;

        int res = 0;
        for (int i=0;i<resultat.size();i++) {

            for (int j=0;j<membres.size();j++) {

                //System.out.println("Comparaison entre "+membres.get(j).getName().toLowerCase()+" et "+resultat.get(i).getLastName().toLowerCase());
                if (membres.get(j).getName().toLowerCase().contains(resultat.get(i).getLastName().toLowerCase())) {

                    resultat.get(i).setId(membres.get(j).getId());
                    //System.out.println("ici, id = "+resultat.get(i).getId());
                    res++;

                }

            }

        }
        //System.out.println(resultat.size()+" noms initialement, "+res+" ont été changés");
        return resultat;
    }



    public void automaticCreation() {
        this.deleteAll();
        ArrayList<Integer> entiers;
        entiers = new ArrayList<Integer>();

        ArrayList<Integer> alreadyAdded;
        alreadyAdded = new ArrayList<Integer>();

        for (int i=0;i<this.liste.controleur.boutons.size();i++) {
            entiers.add(i+1);
        }

        System.out.println(number);
        int numberGroups = this.liste.controleur.membres.size() / number;
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(entiers.size());

        for (int i=0;i<numberGroups;i++) {

            randomGenerator = new Random();
            randomInt = randomGenerator.nextInt(entiers.size());

            for (int j=0;j<number;j++) {

                boolean bool = true;
                while (true) {
                    bool = true;
                    for (int w=0;w<alreadyAdded.size();w++) {
                        //System.out.println(alreadyAdded.get(w));
                        if (alreadyAdded.get(w)==entiers.get(randomInt)) {
                            bool = false; // existe déjà
                            break;
                        }
                    }
                    if (!bool) {
                        randomGenerator = new Random();
                        randomInt = randomGenerator.nextInt(entiers.size());
                    }
                    else {
                        break;
                    }
                }


                //System.out.println(randomInt);
                this.liste.controleur.addStudent(entiers.get(randomInt));
                alreadyAdded.add(entiers.get(randomInt));

                randomGenerator = new Random();
                randomInt = randomGenerator.nextInt(entiers.size());
            }
            addGroup();
        }



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

    private void removeText(int j) {

        //for (int i=0;i<number;)
        //this.namesAdded.remove(j);
        namesAdded.remove(this.textes[j][0].getText());

        for (int x=0;x<this.liste.controleur.boutons.size();x++) {

            this.liste.controleur.boutons.get(x).setDisable(false);
            for (int w=0;w<this.number;w++) {

                if (w<this.namesAdded.size()) {
                    if (namesAdded.get(w).equals(this.liste.controleur.membres.get(x).getName())) {
                        this.liste.controleur.boutons.get(x).setDisable(true);
                    }
                }


                for (int n=0;n<this.groupes2.size();n++) {
                    if (this.liste.controleur.membres.get(x).getName().equals(this.groupes2.get(n).getMember(w))) {
                        this.liste.controleur.boutons.get(x).setDisable(true);
                        //this.namesAdded.add(this.textes[i][0].getText());
                    }
                }
            }


        }

        for (int k=j;k<number-1;k++) {
            this.textes[k][0].setText(this.textes[k+1][0].getText());
            this.textes[k][1].setText(this.textes[k+1][1].getText());
        }

        if (j==0) {
            this.textes[0][0].setText("");
            this.textes[0][1].setText("");
        }
        this.textes[number-1][0].setText("");
        this.textes[number-1][1].setText("");

        if (this.liste.controleur.memberAdded>0) {
            this.liste.controleur.memberAdded--;
        }
    }

    public void deleteAll() {
        //this.groupes2.size() = 0;
        //this.groupes = new Group[100];
        this.groupes2 = new ArrayList<ProjectGroups>();
        Node node = this.groupsTab.getChildren().get(0);
        this.groupsTab.getChildren().clear();
        this.groupsTab.getChildren().add(0,node);

        for (int i=0;i<this.number;i++) {
            removeText(i);
        }

        for (int i=0;i<this.liste.controleur.boutons.size();i++) {
            this.liste.controleur.boutons.get(i).setDisable(false);
        }

        this.namesAdded = new ArrayList<String>();
        /*for (int i=0;i<this.liste.controleur.boutons.size();i++) {
            this.liste.controleur.boutons.get(i).setDisable(false);
        }*/
    }

    public void removeGroup(int pos) {
        //pos = pos-1;
        /*for (int j=pos;j<this.groupes2.size();j++) {
            this.groupes[j] = this.groupes[j+1];
        }
        this.groupes[this.groupes2.size()] = null;*/
        for (int i=0;i<groupes2.get(pos).getNumber();i++) {
            for (int j=0;j<liste.controleur.membres.size();j++) {
                if (this.liste.controleur.membres.get(j).getName().equals(this.groupes2.get(pos).getMember(i))) {
                    this.liste.controleur.boutons.get(j).setDisable(false);
                }
            }
        }
        this.groupes2.remove(pos);
        redo();
    }
    public void addGroup() {
        //Group groupe = new Group(this.groupes2.size()+1,"Dreamteam",number);
        //this.groupes[this.groupes2.size()+1] = groupe;
        //this.groupes2.add(groupe);
        //this.groupes2.size()++;
        boolean groupOK = true;
        for (int i=0;i<number;i++) {
            if ((textes[i][0].getText().length()==0) || (textes[i][1].getText().length()==0)) {
                groupOK = false;
                groupLabel.setText("Les noms ne sont pas valables.");
                break;
            }
        }
        ProjectGroups groupe = new ProjectGroups(this.groupes2.size()+1,"Dreamteam",number);
        for (int i=0;i<number;i++) {
            if (groupOK) {
                MemberInformations membre = new MemberInformations(textes[i][0].getText(), textes[i][1].getText(), "test@test.com", -1);
                groupe.addMember(membre);
                textes[i][0].setText("");
                textes[i][1].setText("");
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
            this.groupes2.add(groupe);
            System.out.println(groupe.members.size());
            addGroup(groupe, this.groupes2.size());
        }
    }

    private void addGroup(ProjectGroups groupe, int pos) {

        this.liste.controleur.memberAdded = 0;

        for (int j=0;j<liste.controleur.boutons.size();j++) {
            this.liste.controleur.boutons.get(j).setDisable(false);

            for (int i=0;i<number;i++) {

                if (this.liste.controleur.membres.get(j).getLastName().equals(this.groupes2.get(pos-1).getMember(i))) {
                    this.liste.controleur.boutons.get(j).setDisable(true);
                    //this.namesAdded.add(this.textes[i][0].getText());
                }

                for (int n=0;n<this.groupes2.size();n++) {
                    if (this.liste.controleur.membres.get(j).getLastName().equals(this.groupes2.get(n).getMember(i))) {
                        this.liste.controleur.boutons.get(j).setDisable(true);
                        //this.namesAdded.add(this.textes[i][0].getText());
                    }
                }
            }

        }

        Label nom = new Label(groupe.name);
        nom.setPadding(new Insets(10, 10, 10, 10));
        //this.groupes2.size()++;
        nom.setPrefHeight(30);
        groupsTab.add(nom,0,pos);
        for (int n=1;n<number+1;n++) {
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

        this.namesAdded = new ArrayList<String>();

        this.taille = (this.groupes2.size()+1)*30+(this.groupes2.size())*2;
        groupsTab.setPrefSize(390,taille);
    }

    private void addGitGroup(ProjectGroups groupe, int pos) {

        this.liste.controleur.memberAdded = 0;

        /*for (int j=0;j<liste.controleur.boutons.size();j++) {
            this.liste.controleur.boutons.get(j).setDisable(false);

            for (int i=0;i<number;i++) {

                if (this.liste.controleur.membres.get(j).getLastName().equals(this.groupes2.get(pos-1).getMember(i))) {
                    this.liste.controleur.boutons.get(j).setDisable(true);
                    //this.namesAdded.add(this.textes[i][0].getText());
                }

                for (int n=0;n<this.groupes2.size();n++) {
                    if (this.liste.controleur.membres.get(j).getLastName().equals(this.groupes2.get(n).getMember(i))) {
                        this.liste.controleur.boutons.get(j).setDisable(true);
                        //this.namesAdded.add(this.textes[i][0].getText());
                    }
                }
            }

        }*/

        Label nom = new Label(groupe.name);
        nom.setPadding(new Insets(10, 10, 10, 10));
        //this.groupes2.size()++;
        nom.setPrefHeight(30);
        groupsTab.add(nom,0,pos);
        for (int n=1;n<groupe.members.size();n++) {
            Label membre = new Label(groupe.getMember(n-1));
            membre.setPadding(new Insets(10, 10, 10, 10));
            membre.setPrefHeight(30);
            groupsTab.add(membre,n,pos);
        }
        /*boutons[this.groupes2.size()-1] = new Button("X");
        boutons[this.groupes2.size()-1].setPadding(new Insets(10, 10, 10, 10));
        //int i = this.groupes2.size();
        boutons[this.groupes2.size()-1].setOnAction(e -> removeGroup(pos-1));
        groupsTab.add(boutons[this.groupes2.size()-1],groupe.number+1,pos);*/

        this.namesAdded = new ArrayList<String>();

        this.taille = (this.groupes2.size()+1)*30+(this.groupes2.size())*2;
        groupsTab.setPrefSize(390,taille);
    }

    public void showStudentList() {
        try {
            this.studentListButton.setDisable(true);
            this.liste = new StudentListView(this.groupe, this);
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

        /*ArrayList<MemberInformations> membresInfo;
        membresInfo = new ArrayList<MemberInformations>();
        membresInfo.add(new MemberInformations("Lefort","Maxence","maxence.lefort@telecomnancy.eu",-1));
        membresInfo.add(new MemberInformations("Sernit","Jérémy","jeremy.sernit@telecomnancy.eu",-1));
        membresInfo.add(new MemberInformations("Lefeuvre","Quentin","quentin.lefeuvre@telecomnancy.eu",-1));
        membresInfo.add(new MemberInformations("Kar","Matta","matta.kar@telecomnancy.eu",-1));

        membresInfo.add(new MemberInformations("Hadjam","Erwan","erwan.hadjam@telecomnancy.eu",-1));
        membresInfo.add(new MemberInformations("Ricci","Yann","yann.ricci@telecomnancy.eu",-1));
        membresInfo.add(new MemberInformations("Trousseu","Mathieu","mathieu.trousseu@telecomnancy.eu",-1));
        membresInfo.add(new MemberInformations("Courtot","Clément","clement.courtot@telecomnancy.eu",-1));*/



        //List<Member> membersLoaded;
        this.membersLoaded = new ArrayList<User>();
        try {
            this.membersLoaded = this.gitlab.getUserApi().getUsers();
        } catch (GitLabApiException e) {
            e.printStackTrace();
        }



        try {
            this.studentListButton.setDisable(true);
            this.liste = new StudentListView(this.groupe, this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        validateGroups.setOnAction(e -> createGitGroups());
        this.removeButtons = new ArrayList<Button>();
        this.namesAdded = new ArrayList<String>();
        this.groupes2 = new ArrayList<ProjectGroups>();
        this.number = GroupConfiguration.getById(groupe.getId()).getNbMembers();
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

            Button remove = new Button("X");
            //remove.setPadding(new Insets(10, 10, 10, 10));
            remove.setMaxHeight(20);
            remove.setPrefHeight(20);
            remove.setMaxWidth(20);
            remove.setMaxWidth(20);
            final int j = i;
            remove.setOnAction(e -> removeText(j));
            this.removeButtons.add(remove);

            textes[i][0] = text1;
            TextFieldTab.add(text1,0,i);
            textes[i][1] = text2;
            TextFieldTab.add(text2,1,i);
            TextFieldTab.add(remove,2,i);
        }

        List<Project> projets;
        projets = new ArrayList<Project>();
        try {
            projets = gitlab.getGroupApi().getProjects(groupe.getId());
        } catch (GitLabApiException e) {
            e.printStackTrace();
        }
        System.out.println(projets.size());
        for (int j=0;j<projets.size();j++) {
            ProjectGroups projet;
            projet = new ProjectGroups(projets.get(j).getId(), projets.get(j).getName(), this.number);
            List<Member> members;
            members = new ArrayList<Member>();
            try {
                members = gitlab.getProjectApi().getMembers(projets.get(j).getId());
            } catch (GitLabApiException e) {
                e.printStackTrace();
            }
            for (int k=0;k<members.size();k++) {
                MemberInformations member;
                System.out.println(members.size());
                member = new MemberInformations(members.get(k).getName(),members.get(k).getName(),null, projets.get(j).getId());
                members.get(k).getName();
                projet.addMember(member);
            }
            addGitGroup(projet, j+1);
        }

    }

    private void createGitGroups() {

        List<Project> projets;
        projets = new ArrayList<Project>();
        try {
            projets = gitlab.getGroupApi().getProjects(groupe.getId());
        } catch (GitLabApiException e) {
            e.printStackTrace();
        }

        //System.out.println(projets.get(projets.size()-1).getId());

        if (groupes2.size()==0) {
            //System.out.println("Aucun groupe n'a été rentré.");
        }
        else {

            Project test = new Project();

            this.groupes2.get(0).members = compare(membersLoaded, this.groupes2.get(0).members);


            //System.out.println(this.groupes2.get(0).members.get(0).getLastName());
            Project projet = new Project();
            projet.setName(this.groupes2.get(0).getName());

            //System.out.println(projets.size());
            //System.out.println(projets.get(1).getId());
            //System.out.println(projets.get(1).getName());

            projets.add(projet);
            groupe.setProjects(projets);
            try {

                test = gitlab.getProjectApi().createProject(groupe.getId(),"Projet 42");
            } catch (GitLabApiException e) {
                e.printStackTrace();
            }
            try {
                gitlab.getGroupApi().updateGroup(groupe.getId(),groupe.getName(),groupe.getPath(),groupe.getDescription(),groupe.getRequestAccessEnabled(),groupe.getRequestAccessEnabled(),groupe.getVisibility(),groupe.getRequestAccessEnabled(),groupe.getRequestAccessEnabled(),groupe.getParentId(),groupe.getSharedRunnersMinutesLimit());
            } catch (GitLabApiException e) {
                e.printStackTrace();
            }


            try {
                projets = gitlab.getGroupApi().getProjects(groupe.getId());
            } catch (GitLabApiException e) {
                e.printStackTrace();
            }

            //System.out.println(projets.size());
            //System.out.println(projets.get(1).getId());
            //System.out.println(projets.get(1).getName());
            for (int i=0;i<this.groupes2.get(0).number;i++) {
                try {
                    //System.out.println("là, id = "+this.groupes2.get(0).members.get(i).getId());
                    //gitlab.getProjectApi().getProject()
                    gitlab.getProjectApi().addMember(test.getId(), this.groupes2.get(0).members.get(i).getId(), AccessLevel.MASTER);
                } catch (GitLabApiException e) {
                    e.printStackTrace();
                }

            }
        }

        //GroupConfiguration groupe2 = GroupConfiguration.getById(groupe.getId());
        //gitlab.getGroupApi().getGroup(groupe.getId())

    }

}