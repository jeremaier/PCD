package eu.telecomnancy.pcd2k17;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.Member;

import java.io.IOException;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.List;


// besoin de public "List<Commit> getCommits(int projectId)"

public class GroupstatView {

    @FXML
    Accordion accordion= new Accordion();
    SplitPane splitpane= new SplitPane();

    final static Logger log = LogManager.getLogger(GroupstatView.class);

    public GroupstatView (GitLabApi gitLab,int projectID) throws Exception{

        try {
            List<Commit> listCommits = gitLab.getCommitsApi().getCommits(projectID);
            List<Member> list = gitLab.getGroupApi().getMembers(projectID);
            final TitledPane[] tps = new TitledPane[list.size()];
            final GridPane[] grid =new GridPane[list.size()];
            final GridPane gridglob= new GridPane();

            int[] tabnb = new int[list.size()];
            Date[] tabdate = new Date[list.size()];
            Date maxDate = new Date() ;

            for (Commit p : listCommits){
                int k=0;
                for (Member l : list){
                    if (p.getAuthor().getName().equals(l.getName()) ){
                        tabnb[k]+=1;
                        if (tabdate[k].before(p.getCommittedDate())) {
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
                Label label3= new Label(tabdate[k].toString());
                GridPane.setConstraints(label3,1,0);
                Label label4= new Label(Integer.toString(tabnb[k]));
                GridPane.setConstraints(label4,1,1);
                grid[i].getChildren().addAll(label1,label2,label3,label4);
                tps[i]=new TitledPane(p.getName(), grid[i]);
                i++;
                k++;
            }



            Label label1glob= new Label("Date du dernier commit du groupe");
            Label label2glob= new Label("nombre de commits realisés par le groupe");
            Label label3glob= new Label(maxDate.toString());
            Label label4glob= new Label(Integer.toString(listCommits.size()));
            Label nomgroupe = new Label(Integer.toString(projectID));

            GridPane.setConstraints(nomgroupe,0,0);
            GridPane.setConstraints(label1glob,0,1);
            GridPane.setConstraints(label2glob,0,2);
            GridPane.setConstraints(label3glob,2,1);
            GridPane.setConstraints(label4glob,1,2);


            CategoryAxis xAxis    = new CategoryAxis();
            xAxis.setLabel("Autheurs");

            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("nb Commits");

            BarChart barChart = new BarChart(xAxis, yAxis);

            XYChart.Series dataSeries1 = new XYChart.Series();
            dataSeries1.setName("2014");
            int K=0;
            for (Member p : list) {
                dataSeries1.getData().add(new XYChart.Data(p.getName(), tabnb[K]));

            }
            barChart.getData().add(dataSeries1);
            GridPane.setConstraints(barChart,0,3);

            gridglob.getChildren().addAll(label1glob,label2glob,label3glob,label4glob,barChart);
            accordion.getPanes().addAll(tps);
            accordion.setExpandedPane(tps[0]);
            splitpane.getItems().add(gridglob);
            splitpane.getItems().add(accordion);

            } catch (GitLabApiException e) {
                e.printStackTrace();
            }



        Stage window = new Stage();
        window.setTitle("SchoolRoom - Statistique du groupe");
        Scene scene = new Scene(new Group(), 800, 600);


        window.setOnCloseRequest(event -> {
            log.debug("terminating application.");
            Platform.exit();
        });



        javafx.scene.Group root = (Group)scene.getRoot();
        root.getChildren().add(accordion);
        window.setScene(scene);
        //window.setScene(new Scene(root, 800, 600));
        window.show();

    }


}