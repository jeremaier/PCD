package eu.telecomnancy.pcd2k17;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;

import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;

import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.Member;
import org.gitlab4j.api.models.Project;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import java.util.List;


public class GroupstatView {

    @FXML
            GridPane gridglob = new GridPane();
    Accordion accordion= new Accordion();
    SplitPane splitpane= new SplitPane();
    SplitPane splitpanebis=new SplitPane();
    String a;
    AnchorPane anchor=new AnchorPane();


    @FXML    private AnchorPane global_data;
    @FXML    private AnchorPane individual_data;
    @FXML    private AnchorPane chart1;
    @FXML    private AnchorPane chart2;
    @FXML    private AnchorPane chart3;


    final static Logger log = LogManager.getLogger(GroupstatView.class);


    public GroupstatView (GitLabApi gitLab,int projectID) throws Exception{



            Stage primaryStage =new Stage();

            primaryStage.setTitle("SchoolRoom statistique de groupe");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Groupstat.fxml"));
            loader.setControllerFactory(iC -> new GroupstatController(gitLab,projectID));
            Parent root = loader.load();

            primaryStage.setOnCloseRequest(event -> {
                log.debug("terminating application.");
                primaryStage.close();
            });


            primaryStage.setScene(new Scene(root, 800, 700));
            primaryStage.show();

    }






}