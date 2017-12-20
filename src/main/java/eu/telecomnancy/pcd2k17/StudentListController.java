package eu.telecomnancy.pcd2k17;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.gitlab4j.api.models.Project;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StudentListController implements Initializable {

    public Project project;

    public int memberAdded;

    @FXML
    private GridPane studentTab;

    public ArrayList<Button> boutons;

    public ArrayList<MemberInformations> membres;

    public ProjectsController groupes;

    public StudentListController(Project project, ProjectsController groupes){
        this.project = project;
        this.boutons = new ArrayList<Button>();
        this.groupes = groupes;
    }

    public void addStudent(int number) {

    //System.out.println(memberAdded+","+groupes.number);
        memberAdded = this.groupes.namesAdded.size();
        //System.out.println(memberAdded);
        if ((memberAdded < groupes.number)&&memberAdded>=0) {
            groupes.textes[memberAdded][0].setText(membres.get(number-1).getLastName());
            groupes.textes[memberAdded][1].setText(membres.get(number-1).getFirstname());

            boutons.get(number-1).setDisable(true);
            this.groupes.namesAdded.add(groupes.textes[memberAdded][0].getText());
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

        membres = new ArrayList<MemberInformations>();
        membres.add(new MemberInformations("Lefort","Maxence","maxence.lefort@telecomnancy.eu"));
        membres.add(new MemberInformations("Sernit","Jérémy","jeremy.sernit@telecomnancy.eu"));
        membres.add(new MemberInformations("Lefeuvre","Quentin","quentin.lefeuvre@telecomnancy.eu"));
        membres.add(new MemberInformations("Kar","Matta","matta.kar@telecomnancy.eu"));

        membres.add(new MemberInformations("Hadjam","Erwan","erwan.hadjam@telecomnancy.eu"));
        membres.add(new MemberInformations("Ricci","Yann","yann.ricci@telecomnancy.eu"));
        membres.add(new MemberInformations("Trousseu","Mathieu","mathieu.trousseu@telecomnancy.eu"));
        membres.add(new MemberInformations("Courtot","Clément","clement.courtot@telecomnancy.eu"));

        int taille;
        taille = (membres.size()+1)*30+(membres.size())*10;
        studentTab.setPrefSize(500,taille);

        Label ajoute = new Label("Ajouter");
        ajoute.setPadding(new Insets(10, 10, 10, 10));
        ajoute.setPrefHeight(30);

        Label nomEx = new Label("Nom");
        nomEx.setPadding(new Insets(10, 10, 10, 10));
        nomEx.setPrefHeight(30);

        Label prenomEx = new Label("Prénom");
        prenomEx.setPadding(new Insets(10, 10, 10, 10));
        prenomEx.setPrefHeight(30);

        Label mailEx = new Label("E-mail");
        mailEx.setPadding(new Insets(10, 10, 10, 10));
        mailEx.setPrefHeight(30);

        studentTab.add(ajoute,0,0);
        studentTab.add(nomEx,1,0);
        studentTab.add(prenomEx,2,0);
        studentTab.add(mailEx,3,0);

        for (int i=0;i<membres.size();i++) {
            Label nom = new Label(membres.get(i).getLastName());
            nom.setPadding(new Insets(10, 10, 10, 10));
            nom.setPrefHeight(30);

            Label prenom = new Label(membres.get(i).getFirstname());
            prenom.setPadding(new Insets(10, 10, 10, 10));
            prenom.setPrefHeight(30);

            Label mail = new Label(membres.get(i).getEmail());
            mail.setPadding(new Insets(10, 10, 10, 10));
            mail.setPrefHeight(30);


            boutons.add(new Button("+"));
            boutons.get(i).setPadding(new Insets(10, 10, 10, 10));
            //int i = this.groupes2.size();
            final int j = i+1;
            boutons.get(i).setOnAction(e -> addStudent(j));
            studentTab.add(boutons.get(i),0,i+1);
            studentTab.setHalignment(boutons.get(i), HPos.CENTER);

            studentTab.add(nom,1,i+1);
            studentTab.add(prenom,2,i+1);
            studentTab.add(mail,3,i+1);
        }

    }

}
