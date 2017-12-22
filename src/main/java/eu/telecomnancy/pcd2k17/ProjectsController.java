package eu.telecomnancy.pcd2k17;

import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
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
    private int taille;

    public int shift;
    public TextField[][] textes;
    public ArrayList<String> namesAdded;
    //public Group[] groupes;
    public ArrayList<ProjectGroups> groupes2;
    public Button[] boutons;
    public StudentListView liste;
    public Group groupe;
    public ArrayList<Button> removeButtons;
    public GitLabApi gitlab;

    public ArrayList<Integer> studentsAdded;

    public List<User> membersLoaded;

    public List<Project> projetsGit;

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

    public ArrayList<MemberInformations> compare2(ArrayList<MemberInformations> membresInfo) {
        List<User> resultat;

        int res = 0;
        for (int i=0;i<membresInfo.size();i++) {
            resultat = new ArrayList<User>();
            try {
                resultat = gitlab.getUserApi().findUsers(membresInfo.get(i).getEmail());
            } catch (GitLabApiException e) {
                e.printStackTrace();
            }

            if (resultat.size()==1) {
                membresInfo.get(i).setId(resultat.get(0).getId());
            }
        }
        return membresInfo;
    }

    public int getMemberIndex(Member membre) {

        for (int i=0;i<this.liste.controleur.boutons.size();i++) {

            if (membre.getUsername().toLowerCase().contains(this.liste.controleur.membres.get(i).getLastName().toLowerCase())) {
                return i;
            }

        }
        return -1;
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

        int numberGroups = this.liste.controleur.membres.size() / number;
        numberGroups = numberGroups - projetsGit.size()*this.number;
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
                        if ((alreadyAdded.get(w)==entiers.get(randomInt)) || (this.liste.controleur.boutons.get(randomInt).isDisabled()==true)) {
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

        if ((j>=0)&&(j<studentsAdded.size())) {
            studentsAdded.remove(j);
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
        printGitProjects();
    }

    public void removeGroup(int pos) {

        for (int i=0;i<groupes2.get(pos).members.size();i++) {
            for (int j=0;j<liste.controleur.membres.size();j++) {
                if (this.liste.controleur.membres.get(j).getLastName().equals(this.groupes2.get(pos).members.get(i).getLastName())) {
                    this.liste.controleur.boutons.get(j).setDisable(false);
                }
            }
        }
        this.groupes2.remove(pos);
        redo();
        printGitProjects();
    }
    public void addGroup() {
        boolean groupOK = true;
        for (int i=0;i<number;i++) {
            if ((textes[i][0].getText().length()==0) || (textes[i][1].getText().length()==0)) {
                groupOK = false;
                groupLabel.setText("Les noms ne sont pas valables.");
                break;
            }
        }
        ProjectGroups groupe = new ProjectGroups(this.groupes2.size()+1,"Dreamteam",number);

        if (nameGroupTF.getText().length()==0) {
            boolean bool = isNameAvailable("Groupe "+groupe.id);
            while (!bool) {
                groupe.id = groupe.id + 1;
                bool = isNameAvailable("Groupe "+groupe.id);
            }
            groupe.setName("Groupe "+groupe.id);
        }
        else {
            if ((isNameAvailable(nameGroupTF.getText()))&&(!groupe.getName().equals("Dreamteam"))) {
                groupe.setName(nameGroupTF.getText());
            }
            else {

                groupOK = false;
                groupLabel.setText("Le nom de groupe existe déjà.");
                nameGroupTF.setText("");
                nameGroupTF.setPromptText("Nom du groupe");
            }
        }


        for (int i=0;i<number;i++) {
            if (groupOK) {
                MemberInformations membre = new MemberInformations(this.liste.controleur.membres.get(studentsAdded.get(i)).getLastName(), this.liste.controleur.membres.get(studentsAdded.get(i)).getName(), this.liste.controleur.membres.get(studentsAdded.get(i)).getEmail());
                groupe.addMember(membre);
                textes[i][0].setText("");
                textes[i][1].setText("");
            }
        }


        if (groupOK) {
            groupLabel.setText("");
            nameGroupTF.setText("");
            nameGroupTF.setPromptText("Nom du groupe");
            this.groupes2.add(groupe);
            addGroup(groupe, this.groupes2.size());
            this.studentsAdded = new ArrayList<Integer>();
        }
    }

    private boolean isNameAvailable(String s) {

        boolean res = true;
        for (int i=0;i<projetsGit.size();i++) {

            if (projetsGit.get(i).getName().toLowerCase().equals(s.toLowerCase())) {
                res = false;
            }

        }

        for (int j=0;j<groupes2.size();j++) {
            if (groupes2.get(j).getName().toLowerCase().equals(s.toLowerCase())) {
                res = false;
            }
        }
        return res;

    }

    private void addGroup(ProjectGroups groupe, int pos) {


        System.out.println("Etape 1");
        this.liste.controleur.memberAdded = 0;

        for (int j=0;j<liste.controleur.boutons.size();j++) {
            this.liste.controleur.boutons.get(j).setDisable(false);

            for (int i=0;i<number;i++) {

                if (this.liste.controleur.membres.get(j).getLastName().equals(this.groupes2.get(pos-1).getMember(i))) {
                    this.liste.controleur.boutons.get(j).setDisable(true);
                }

                for (int n=0;n<this.groupes2.size();n++) {
                    if (this.liste.controleur.membres.get(j).getLastName().equals(this.groupes2.get(n).getMember(i))) {
                        this.liste.controleur.boutons.get(j).setDisable(true);
                    }
                }
            }

        }

        pos = pos+shift;
        Label nom = new Label(groupe.name);
        nom.setPadding(new Insets(10, 10, 10, 10));
        //this.groupes2.size()++;
        nom.setPrefHeight(30);

        FlowPane pane = new FlowPane();
        Label nonValide = new Label("Non créé sur Gitlab");
        //nonValide.setPadding(new Insets(10, 10, 10, 10));
        //this.groupes2.size()++;
        nonValide.setPrefHeight(30);
        pane.setPrefWidth(100);
        pane.getChildren().add(nonValide);
        pane.setStyle("-fx-background-color: #DC143C;-fx-border-color : black;");

        System.out.println("Etape 2");
        groupsTab.add(nom,0,pos);
        groupsTab.add(pane, 1, pos);
        Label membre = new Label();
        for (int n=2;n<number+2;n++) {
            membre = new Label(groupe.getMember(n-2));
            membre.setPadding(new Insets(10, 10, 10, 10));
            membre.setPrefHeight(30);
            groupsTab.add(membre,n,pos);
        }
        final int pos2 = pos-shift;
        boutons[this.groupes2.size()-1] = new Button("X");
        boutons[this.groupes2.size()-1].setMaxWidth(5);
        boutons[this.groupes2.size()-1].setStyle("-fx-font-size : 10");
        //boutons[this.groupes2.size()-1].setPadding(new Insets(10, 10, 10, 10));
        //int i = this.groupes2.size();
        boutons[this.groupes2.size()-1].setOnAction(e -> removeGroup(pos2-1));
        groupsTab.add(boutons[this.groupes2.size()-1],number+2,pos);
        //printGitProjects();

        System.out.println("Etape 3");

        this.namesAdded = new ArrayList<String>();

        this.taille = (this.groupes2.size()+shift+1)*30+(this.groupes2.size()+shift)*2;
        groupsTab.setPrefSize(390,taille);
    }

    private void addGitGroup(ProjectGroups groupe, int pos) {

        this.liste.controleur.memberAdded = 0;

        Label nom = new Label(groupe.name);
        nom.setPadding(new Insets(10, 10, 10, 10));
        nom.setMaxHeight(30);


        FlowPane pane = new FlowPane();
        Label valide = new Label("Créé sur Gitlab");
        //nonValide.setPadding(new Insets(10, 10, 10, 10));
        //this.groupes2.size()++;
        valide.setPrefHeight(30);
        pane.setPrefWidth(100);
        pane.getChildren().add(valide);
        pane.setStyle("-fx-background-color: #3cb371;-fx-border-color : black;");
        //Label valide = new Label("Créé sur Gitlab");
        //valide.setPadding(new Insets(10, 10, 10, 10));
        valide.setPrefHeight(30);

        groupsTab.add(nom,0,pos);
        groupsTab.add(pane,1,pos);
        for (int n=2;n<groupe.members.size()+2;n++) {
            Label membre = new Label(groupe.members.get(n-2).getLastName());
            membre.setPadding(new Insets(10, 10, 10, 10));
            membre.setPrefHeight(30);
            groupsTab.add(membre,n,pos);
        }

        this.namesAdded = new ArrayList<String>();

        this.taille = (this.groupes2.size()+shift+1)*30+(this.groupes2.size()+shift)*2;
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

        projetsGit = new ArrayList<Project>();

        this.studentsAdded = new ArrayList<Integer>();

        List<Project> projets;
        projets = new ArrayList<Project>();
        try {
            projets = gitlab.getGroupApi().getProjects(groupe.getId());
        } catch (GitLabApiException e) {
            e.printStackTrace();
        }
        projetsGit = projets;
        this.shift = projets.size();

        //List<Member> membersLoaded;
        this.membersLoaded = new ArrayList<User>();
        try {
            this.membersLoaded = this.gitlab.getUserApi().getUsers(0, 200);
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
        textes = new TextField[number][2];
        taille = (number)*30+(number-1)*5;
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

        printGitProjects();

        this.groupsTab.setStyle("-fx-background-color: #DCDCDC;");

    }

    private void printGitProjects() {

        for (int j=0;j<projetsGit.size();j++) {
            ProjectGroups projet;
            projet = new ProjectGroups(projetsGit.get(j).getId(), projetsGit.get(j).getName(), this.number);
            List<Member> members;
            members = new ArrayList<Member>();
            try {
                members = gitlab.getProjectApi().getMembers(projetsGit.get(j).getId());
            } catch (GitLabApiException e) {
                e.printStackTrace();
            }
            for (int k=0;k<members.size();k++) {
                MemberInformations member;

                member = new MemberInformations(members.get(k).getName(),members.get(k).getName(),members.get(k).getEmail());

                if (getMemberIndex(members.get(k))!=-1) {
                    member.setLastName(this.liste.controleur.membres.get(getMemberIndex(members.get(k))).getLastName());
                    this.liste.controleur.boutons.get(getMemberIndex(members.get(k))).setDisable(true);
                }

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


        if (groupes2.size()==0) {
        }
        else {

            for (int k=0;k<groupes2.size();k++) {

                Project test = new Project();

                this.groupes2.get(k).members = compare2(this.groupes2.get(k).members);

                Project projet = new Project();
                projet.setName(this.groupes2.get(k).getName());

                projets.add(projet);
                groupe.setProjects(projets);
                try {

                    test = gitlab.getProjectApi().createProject(groupe.getId(),groupes2.get(k).name);
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

                for (int i=0;i<this.groupes2.get(k).number;i++) {
                    try {
                        gitlab.getProjectApi().addMember(test.getId(), this.groupes2.get(k).members.get(i).getId(), AccessLevel.MASTER);
                    } catch (GitLabApiException e) {
                        e.printStackTrace();
                    }

                }

            }
        }

    }
}