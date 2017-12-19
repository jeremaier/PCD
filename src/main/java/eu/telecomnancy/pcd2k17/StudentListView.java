package eu.telecomnancy.pcd2k17;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gitlab4j.api.models.Project;
//import Group;

public class StudentListView {

    final static Logger log = LogManager.getLogger(Main.class);

    public StudentListView(Project project) throws Exception{

        Stage window = new Stage();
        window.setTitle("Liste des élèves");
        Scene scene = new Scene(new Group(), 800, 600);

        /*FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("StudentListView.fxml"));
        loader.setControllerFactory(iC -> new GroupsViewController(project));
        Parent root = loader.load();*/

        window.setOnCloseRequest(event -> {
            log.debug("terminating application.");
            Platform.exit();
        });


        window.setScene(scene);
        //window.setScene(new Scene(root, 800, 600));
        window.show();

    }
}
