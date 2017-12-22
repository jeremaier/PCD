package eu.telecomnancy.pcd2k17;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.chart.*;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.Group;
import org.gitlab4j.api.models.Member;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class GroupstatController implements Initializable {

    GitLabApi gitLab;
    int projectID;

    List<MemberInformations> liste;

    GroupConfiguration groupconf;


    @FXML
    GridPane gridglob = new GridPane();
    Accordion accordion= new Accordion();
    SplitPane splitpane= new SplitPane();
    SplitPane splitpanebis=new SplitPane();
    String a;
    AnchorPane anchor=new AnchorPane();


    @FXML    private AnchorPane global_data;
    @FXML    private AnchorPane stat_indiv;
    @FXML    private AnchorPane chart1;
    @FXML    private AnchorPane chart2;
    @FXML    private AnchorPane chart3;
    @FXML    private AnchorPane charthaut;
    @FXML    private Label groupNom;


    public GroupstatController(GitLabApi gitLab,int projectID, GroupConfiguration groupconf ){
        this.gitLab=gitLab;
        this.projectID=projectID;
        this.groupconf= groupconf;

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        try {
            List<Commit> listCommits = gitLab.getCommitsApi().getCommits(projectID);
            List<org.gitlab4j.api.models.Member> list = gitLab.getProjectApi().getMembers(projectID);

            ArrayList<MemberInformations> liste = groupconf.getMembersList();
            final TitledPane[] tps = new TitledPane[list.size()];
            final GridPane[] grid = new GridPane[list.size()];
            final GridPane gridglob = new GridPane();

            int[] tabnb = new int[list.size()];
            Date[] tabdate = new Date[list.size()];
            Date maxDate = null;
            int[] tabsemaine= new int[7];
            int[] tabjour=new int[24];


            int[][] commitshistory = new int[365][list.size()];

            for (Commit p : listCommits) {
                int k = 0;
                LocalDate dateter = p.getAuthoredDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                tabsemaine[dateter.getDayOfWeek().getValue()]+=1;
                for (org.gitlab4j.api.models.Member m : list) {
                    LocalDate datebis = p.getAuthoredDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    if (p.getAuthorName().equals(m.getName())) {
                        commitshistory[datebis.getDayOfYear()][k] += 1;
                    }
                    k++;
                }
            }


            // ProjectConfiguration.getById(projectID).getMembers().getEmail();






            String[] listmailord = new String[list.size()];
            int k=0;
            for (Member M : list){

                for (MemberInformations m : liste){
                    if (M.getName().toLowerCase().equals(m.getLastName().toLowerCase()+ " " + m.getFirstname().toLowerCase())){

                        listmailord[k]=m.getEmail();
                        k++;
                    }
                }
            }




            for (Commit p : listCommits){

                for (int k3=0;k3<list.size();k3++){


                    if (maxDate==null){
                        maxDate=p.getCommittedDate();
                    }
                    if (p.getCommitterEmail().equals(listmailord[k3]) ){
                        tabnb[k3]+=1;
                        if (tabdate[k3]==null){
                            tabdate[k3] = p.getCommittedDate();

                        }
                        else if (tabdate[k3].before(p.getCommittedDate())) {
                            tabdate[k3] = p.getCommittedDate();
                            if (p.getCommittedDate().after(maxDate)){
                                maxDate=p.getCommittedDate();
                            }
                        }

                    }
                }
            }




            int i = 0;
            int kgb = 0;
            for (org.gitlab4j.api.models.Member p : list) {

                grid[i] = new GridPane();

                Label label1 = new Label("Date du dernier commit");
                GridPane.setConstraints(label1, 0, 0);
                Label label2 = new Label("Nombre de commits realisés ");
                GridPane.setConstraints(label2, 0, 1);

                if (tabdate[kgb] == null) {
                    a = "aucun commit";
                } else {
                    a = tabdate[kgb].toString();
                }
                Label label3 = new Label(a);
                GridPane.setConstraints(label3, 1, 0);
                Label label4 = new Label(Integer.toString(tabnb[kgb]));
                GridPane.setConstraints(label4, 1, 1);
                grid[i].getChildren().addAll(label1, label2, label3, label4);
                tps[i] = new TitledPane(p.getName(), grid[i]);
                i++;
                kgb++;
            }


            if (maxDate == null) {
                a = "aucun commit";
            } else {
                a = maxDate.toString();
            }
            Label label1glob = new Label("Dernier commit du groupe");
            Label label2glob = new Label("Nombre de commits groupe");
            Label label3glob = new Label(a);
            Label label4glob = new Label("                                      " + Integer.toString(listCommits.size()));


            GridPane.setConstraints(label1glob, 0, 1);
            GridPane.setConstraints(label2glob, 0, 2);
            GridPane.setConstraints(label3glob, 1, 1);
            GridPane.setConstraints(label4glob, 1, 2);


            LocalDate deb=GroupConfiguration.getById(groupconf.getId()).getFirstDay();
            LocalDate fin=GroupConfiguration.getById(groupconf.getId()).getLastDay();

            int debut = deb.getDayOfYear();
            int fini;

            Date AUJ=new Date();
            int AUJ1 = AUJ.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfYear();

            if (fin==null){

                fini = AUJ1;

            } else {
                if (fin.getDayOfYear()>AUJ1){
                    fini=AUJ1;
                }else{
                    fini = fin.getDayOfYear();
                }

            }


            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Auteurs");
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("nb Commits");

            BarChart barChart = new BarChart(xAxis, yAxis);

            XYChart.Series dataSeries1 = new XYChart.Series();
            dataSeries1.setName("Nombre de commits effectué sur le projet");
            int K = 0;
            for (org.gitlab4j.api.models.Member p : list) {
                dataSeries1.getData().add(new XYChart.Data(p.getName(), tabnb[K]));
                K++;
            }
            barChart.getData().add(dataSeries1);


            gridglob.getChildren().addAll(label1glob, label2glob, label3glob, label4glob);


            final NumberAxis xAxis1 = new NumberAxis(debut-debut, fini-debut, 1);
            final NumberAxis yAxis1 = new NumberAxis();
            final AreaChart<Number, Number> ac =
                    new AreaChart<Number, Number>(xAxis1, yAxis1);
            ac.setTitle("Nombre de commits par jours individuel");

            int k1 = 0;
            for (Member m : list) {
                XYChart.Series seriesApril = new XYChart.Series();
                seriesApril.setName(m.getName());
                for (int u = debut; u < fini+1; u++) {
                    seriesApril.getData().add(new XYChart.Data(u-debut, commitshistory[u][k1]));
                }
                ac.getData().addAll(seriesApril);
                k1++;
            }

            final NumberAxis xAxis2 = new NumberAxis(debut-debut, fini-debut, 1);
            final NumberAxis yAxis2 = new NumberAxis();
            final AreaChart<Number, Number> ac2 = new AreaChart<Number, Number>(xAxis2, yAxis2);
            ac2.setTitle("Nombre de commits par jours du groupe");


            XYChart.Series seriesnovember = new XYChart.Series();
            seriesnovember.setName("groupe");




            for (int u = debut; u < fini+1; u++) {
                int a = 0;
                for (int k2 = 0; k2 < list.size(); k2++) {
                    a = a + commitshistory[u][k2];

                }
                seriesnovember.getData().add(new XYChart.Data(u-debut, a));
            }
            ac2.getData().addAll(seriesnovember);

            CategoryAxis xAx = new CategoryAxis();
            xAxis.setLabel("jour de la semaine");
            NumberAxis yAx = new NumberAxis();
            yAxis.setLabel("nb Commits");

            BarChart barChartjour = new BarChart(xAx, yAx);

            XYChart.Series dataSeries5 = new XYChart.Series();
            dataSeries5.setName("commits/jours");

                dataSeries5.getData().add(new XYChart.Data("Lundi", tabsemaine[1]));
                dataSeries5.getData().add(new XYChart.Data("Mardi", tabsemaine[2]));
                dataSeries5.getData().add(new XYChart.Data("Mercredi", tabsemaine[3]));
                dataSeries5.getData().add(new XYChart.Data("Jeudi", tabsemaine[4]));
                dataSeries5.getData().add(new XYChart.Data("vendredi", tabsemaine[5]));
                dataSeries5.getData().add(new XYChart.Data("samedi", tabsemaine[6]));
                dataSeries5.getData().add(new XYChart.Data("dimanche", tabsemaine[0]));


            barChartjour.getData().add(dataSeries5);



            accordion.getPanes().addAll(tps);

            groupNom.setText(groupconf.getName());

            global_data.getChildren().add(gridglob);
            stat_indiv.getChildren().add(accordion);
            charthaut.getChildren().add(barChart);
            chart1.getChildren().add(ac);
            chart2.getChildren().add(barChartjour);
            chart3.getChildren().add(ac2);

        }
        catch (GitLabApiException e) {
            e.printStackTrace();
        }
    }
}


