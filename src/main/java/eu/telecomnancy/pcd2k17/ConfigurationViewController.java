package eu.telecomnancy.pcd2k17;

import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class ConfigurationViewController implements Initializable {
    final static Logger log = LogManager.getLogger(ConfigurationViewController.class);

    @FXML
    private AnchorPane controllerView;

    @FXML
    private Button AddFile;

    @FXML
    private Button Cancel;

    @FXML
    private Button Archive;

    @FXML
    private Button Save;

    @FXML
    private TextField Title;

    @FXML
    private TextField Module;

    @FXML
    private TextField NbMembers;

    @FXML
    private DatePicker FirstDay;

    @FXML
    private DatePicker LastDay;

    @FXML
    private TextArea Description;

    public final UnaryOperator<TextFormatter.Change> integerOnlyFilter = change -> {
        final String text = change.getText();
        return (text.isEmpty() || text.matches("[0-9]*")) ? change : null;
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final BooleanProperty firstTime = new SimpleBooleanProperty(true);

        NbMembers.setTextFormatter(new TextFormatter(integerOnlyFilter));
        Title.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue && firstTime.get()){
                controllerView.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });
    }

    @FXML
    public void handleClickAddFile(ActionEvent event) {
        log.debug("Add");
    }

    @FXML
    public void handleClickCancel(ActionEvent event) throws IOException {
        log.debug("Cancel");
    }

    @FXML
    public void handleClickArchive(ActionEvent event) {
        log.debug("Archive");
    }

    @FXML
    public void handleClickSave(ActionEvent event) {
        Alert alert;

        ConfigurationView.setProject(Title.getText(), Module.getText(), NbMembers.getText(), FirstDay.getValue(), LastDay.getValue(), Description.getText());

        if(!ConfigurationView.getProject().isComplete()) {
            log.debug("Save");
        } else {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information dialog");
            alert.setHeaderText(null);
            alert.setContentText("* fields must be completed");

            alert.showAndWait();
        }
    }
}
