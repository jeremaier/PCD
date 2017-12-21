package eu.telecomnancy.pcd2k17;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class LogController implements Initializable {

    @FXML TextField password_fill;
    @FXML TextField identifiant_fill;
    @FXML TextField cToken;
    @FXML Button connect_button;
    @FXML ProgressIndicator pi;
    @FXML Label load;
    //////////
    @FXML CheckBox saveToken;

    private String tokenFileName = "Token";
    //////////

    private String connectiontoken;

    final static Logger log = LogManager.getLogger( LogController.class);


    public void TryConnect(ActionEvent event){
        Task task = new Task<Void>() {
            @Override public Void call() {
                pi.setVisible(true);
                load.setVisible(true);
                final int max = 100;
                for (int i=1; i<=max; i++) {
                    if (isCancelled()) {
                        break;
                    }
                    try {
                        sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };
        //pi.progressProperty().bind(task.progressProperty());
        task.stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override public void changed(ObservableValue<? extends Worker.State> observableValue, Worker.State oldState, Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    checkLog();
                    pi.setVisible(false);
                    load.setVisible(false);
                }
            }
        });
        new Thread(task).start();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //////////////////
        String tokenFromFile = this.loadTokenFromFile();

        if(tokenFromFile != null)
            this.cToken.setText(tokenFromFile);

        saveToken.setSelected(true);
        //////////////////

        pi.setProgress(-1);
        pi.setVisible(false);
        load.setVisible(false);
    }



    public void checkLog(){
        log.debug("connection button was clicked!");
        if ((identifiant_fill.getText().equals("admin") ) && (password_fill.getText().equals("admin"))){
            connectiontoken=cToken.getText();

            GitLabApi gla = new GitLabApi("https://gitlab.telecomnancy.univ-lorraine.fr", connectiontoken);
            if(!connectiontoken.equals("")){
                try {
                    List<Project> list = gla.getProjectApi().getMemberProjects();
                    ///////////
                    if(saveToken.isSelected())
                        this.saveTokenInFile();
                    else this.deleteTokenFile();
                    ///////////
                    Stage stage = (Stage) connect_button.getScene().getWindow();
                    stage.close();
                    new GroupView(connectiontoken,identifiant_fill.getText());
                } catch (GitLabApiException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Erreur d'identification");
                    alert.setHeaderText(null);
                    alert.setContentText("Clé de connexion incorrecte");
                    alert.showAndWait();
                    identifiant_fill.clear();
                    password_fill.clear();
                    cToken.clear();
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Erreur d'identification");
                alert.setHeaderText(null);
                alert.setContentText("Veuiller entrer une clé de connexion");
                alert.showAndWait();
                identifiant_fill.clear();
                password_fill.clear();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur d'identification");
            alert.setHeaderText(null);
            alert.setContentText("Identifiant et/ou mot de passe incorrect(s)");
            alert.showAndWait();
            identifiant_fill.clear();
            password_fill.clear();
        }
    }

    /////////////////////
    private String loadTokenFromFile() {
        File file = new File(FileManager.getFilePath(tokenFileName));
        FileInputStream fIn;
        ObjectInputStream oIn;

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (file.length() > 0) {
            try {
                fIn = new FileInputStream(file);
                oIn = new ObjectInputStream(fIn);

                String token = (String) oIn.readObject();

                if (oIn != null)
                    oIn.close();

                if (fIn != null)
                    fIn.close();

                return token;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private void saveTokenInFile() {
        if(connectiontoken != null) {
            FileOutputStream fOut;
            ObjectOutputStream oOut;

            try {
                fOut = new FileOutputStream(FileManager.getFilePath(tokenFileName));
                oOut = new ObjectOutputStream(fOut);

                oOut.writeObject(connectiontoken);

                if (oOut != null)
                    oOut.close();

                if (fOut != null)
                    fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteTokenFile() {
        File tokenFile = new File(FileManager.getFilePath(tokenFileName));

        if(tokenFile.exists())
            tokenFile.delete();
    }
    ////////////////////
}