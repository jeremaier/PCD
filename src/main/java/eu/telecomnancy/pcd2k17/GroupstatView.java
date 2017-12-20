package eu.telecomnancy.pcd2k17;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.*;

import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;

import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.Member;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import java.util.List;


public class GroupstatView {

    @FXML
    Accordion accordion= new Accordion();
    SplitPane splitpane= new SplitPane();
    SplitPane splitpanebis=new SplitPane();
    String a;

    final static Logger log = LogManager.getLogger(GroupstatView.class);


    public GroupstatView (GitLabApi gitLab,int projectID) throws Exception{

        try {

            List<Commit> listCommits = gitLab.getCommitsApi().getCommits(projectID);
            List<Member> list = gitLab.getProjectApi().getMembers(projectID);


            final TitledPane[] tps = new TitledPane[list.size()];
            final GridPane[] grid =new GridPane[list.size()];
            final GridPane gridglob= new GridPane();

            int[] tabnb = new int[list.size()];
            Date[] tabdate = new Date[list.size()];
            Date maxDate=null;


            int[][] commitshistory = new int[365][list.size()];

            for (Commit p : listCommits){
                int k=0;
                for (Member m : list){
                    LocalDate datebis = p.getAuthoredDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    if (p.getAuthorName().equals(m.getName()) ){
                        commitshistory[datebis.getDayOfYear()][k]+=1;
                    }
                    k++;
                }
            }


            // ProjectConfiguration.getById(projectID).getMembers().getEmail();

            for (Commit p : listCommits){

                int k=0;
                for (Member l : list){
                     System.out.println("author email  " + p.getAuthorName());
                     System.out.println("committer email" + p.getCommitterName());
                     System.out.println("member email" + l.getName());

                    if (maxDate==null){
                        maxDate=p.getCommittedDate();
                    }
                    if (p.getCommitterEmail().equals(l.getEmail()) ){
                        tabnb[k]+=1;
                        if (tabdate[k]==null){
                            tabdate[k] = p.getCommittedDate();

                        }
                        else if (tabdate[k].before(p.getCommittedDate())) {
                            tabdate[k] = p.getCommittedDate();
                            if (p.getCommittedDate().after(maxDate)){
                                maxDate=p.getCommittedDate();
                            }
                        }

                    }
                    k+=1;
                }
            }

            int i=0;
            int k = 0;
            for (Member p : list) {

                grid[i]=new GridPane();

                Label label1= new Label("Date du dernier commit");
                GridPane.setConstraints(label1,0,0);
                Label label2= new Label("Nombre de commits realisés ");
                GridPane.setConstraints(label2,0,1);

                if (tabdate[k]==null){
                    a ="aucun commit";
                } else {
                    a = tabdate[k].toString();
                }
                Label label3= new Label(a);
                GridPane.setConstraints(label3,1,0);
                Label label4= new Label(Integer.toString(tabnb[k]));
                GridPane.setConstraints(label4,1,1);
                grid[i].getChildren().addAll(label1,label2,label3,label4);
                tps[i]=new TitledPane(p.getName(), grid[i]);
                i++;
                k++;
            }


            if (maxDate==null){
                a ="aucun commit";
            } else {
                a = maxDate.toString();
            }
            Label label1glob= new Label("Dernier commit du groupe");
            Label label2glob= new Label("Nombre de commits groupe");
            Label label3glob= new Label(a);
            Label label4glob= new Label("                                      "  + Integer.toString( listCommits.size()));



            GridPane.setConstraints(label1glob,0,1);
            GridPane.setConstraints(label2glob,0,2);
            GridPane.setConstraints(label3glob,1,1);
            GridPane.setConstraints(label4glob,1,2);


            CategoryAxis xAxis    = new CategoryAxis();
            xAxis.setLabel("Auteurs");
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("nb Commits");

            BarChart barChart = new BarChart(xAxis, yAxis);

            XYChart.Series dataSeries1 = new XYChart.Series();
            dataSeries1.setName("Nombre de commits effectué sur le projet");
            int K=0;
            for (Member p : list) {
                dataSeries1.getData().add(new XYChart.Data(p.getName(), tabnb[K]));
            K++;
            }
            barChart.getData().add(dataSeries1);


            gridglob.getChildren().addAll(label1glob,label2glob,label3glob,label4glob);


//////////////////////////

            //int start= ProjectConfiguration.getById(projectID).getFirstDay().getDayOfYear();
            //int fin  = ProjectConfiguration.getById(projectID).getLastDay().getDayOfYear();


            final NumberAxis xAxis1 = new NumberAxis(320, 365, 1);
            final NumberAxis yAxis1 = new NumberAxis();
            final AreaChart<Number,Number> ac =
                    new AreaChart<Number,Number>(xAxis1,yAxis1);
            ac.setTitle("Nombre de commits par jours individuel");

            int k1=0;
            for (Member m : list){
                XYChart.Series seriesApril= new XYChart.Series();
                seriesApril.setName(m.getName());
                for (int u=320;u<365;u++){
                    seriesApril.getData().add(new XYChart.Data(u, commitshistory[u][k1]));
                }
                ac.getData().addAll(seriesApril);
                k1++;
            }

            final NumberAxis xAxis2 = new NumberAxis(320, 365, 1);
            final NumberAxis yAxis2 = new NumberAxis(0,2,1);
            final AreaChart<Number,Number> ac2 = new AreaChart<Number,Number>(xAxis2,yAxis2);
            ac2.setTitle("Nombre de commits par jours du groupe");



                XYChart.Series seriesnovember= new XYChart.Series();
                   seriesnovember.setName("groupe");
                for (int u=320;u<365;u++){
                    int a=0;
                    for (int k2=0;k2<list.size();k2++) {
                        a=a+commitshistory[u][k2];

                    }
                    seriesnovember.getData().add(new XYChart.Data(u, a));
                }
                ac2.getData().addAll(seriesnovember);




          //  XYChart.Series seriesMay = new XYChart.Series();
          //  seriesMay.setName("May");
          //   seriesMay.getData().add(new XYChart.Data(1, 20));

/////////////////



            splitpanebis.setOrientation(Orientation.VERTICAL);

            splitpanebis.getItems().add(barChart);
            splitpanebis.getItems().add(ac);
            splitpanebis.getItems().add(gridglob);

            accordion.getPanes().addAll(tps);


            SplitPane sp= new SplitPane();
            sp.setOrientation(Orientation.VERTICAL);
            sp.getItems().add(accordion);
            sp.getItems().add(ac2);
            accordion.setMaxSize(400.0,300.0);
            ac2.setMaxSize(400.0,300.0);
            splitpanebis.setMaxSize(400.0,300.0);
            ac.setMaxSize(400.0,300.0);
            barChart.setMaxSize(400.0,300.0);

            splitpane.getItems().add(splitpanebis);
            splitpane.getItems().add(sp);

            } catch (GitLabApiException e) {
                e.printStackTrace();
            }



        Stage window = new Stage();
        window.setTitle("SchoolRoom - Statistique du groupe");



        window.setOnCloseRequest(event -> {
            log.debug("terminating application.");
            Platform.exit();
        });


      //  Scene scene = new Scene(new Group(), 800, 600);

        //window.setScene(scene);



        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Groupstat.fxml"));
       // loader.setControllerFactory(iC-> new ContentController(root,gitlab));
        AnchorPane anchor = null;
        try {
            anchor = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        anchor.getChildren().add(splitpane);
        javafx.scene.Group root = new Group();
        root.getChildren().add(anchor);
        window.setScene(new Scene(root, 800, 400));
        window.show();


    }






}