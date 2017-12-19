package eu.telecomnancy.pcd2k17;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.Visibility;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.Chronology;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class ConfigurationViewController implements Initializable {
    final static Logger log = LogManager.getLogger(ConfigurationViewController.class);

    private Stage configurationStage;
    private ProjectConfiguration projectConfiguration;
    private Project project;

    private boolean archived = this.project.getArchived();
    private boolean changed = false;
    private boolean radioButtonChanged = false;
    private boolean publicSelected = false;
    private boolean privateSelected = false;

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
    private RadioButton Public;

    @FXML
    private RadioButton Private;

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

    public ConfigurationViewController(Project project) {
        this.project = project;
    }

    public final UnaryOperator<TextFormatter.Change> integerOnlyFilter = change -> {
        final String text = change.getText();
        return (text.isEmpty() || text.matches("[0-9]")) ? change : null;
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final BooleanProperty firstTime = new SimpleBooleanProperty(true);

        if(archived)
            Archive.setText("Desarchiver");
        else Archive.setText("Archiver");

        NbMembers.setTextFormatter(new TextFormatter(integerOnlyFilter));
        Title.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue && firstTime.get()) {
                controllerView.requestFocus();
                firstTime.setValue(false);
            }
        });

        Title.lengthProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                changed = true;
            }
        });

        Module.lengthProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                changed = true;
            }
        });

        NbMembers.lengthProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                changed = true;

                if(newValue.intValue() > oldValue.intValue())
                    if(NbMembers.getText().length() >= 1)
                        NbMembers.setText(NbMembers.getText().substring(0, 1));
            }
        });

        FirstDay.valueProperty().addListener(new ChangeListener<LocalDate>() {
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                changed = true;
            }
        });

        LastDay.valueProperty().addListener(new ChangeListener<LocalDate>() {
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                changed = true;
            }
        });

        Description.lengthProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                changed = true;
            }
        });
    }

    @FXML
    public void handleClickPublic(ActionEvent event) {
        this.radioButtonChanged = this.Public.isSelected() != publicSelected ? true : false;
    }

    @FXML
    public void handleClickPrivate(ActionEvent event) {
        this.radioButtonChanged = this.Private.isSelected() != privateSelected ? true : false;
    }

    @FXML
    public void handleClickAddFile(ActionEvent event) {
        log.debug("Add");
    }

    @FXML
    public void handleClickCancel(ActionEvent event) throws IOException {
        log.debug("Cancel");
        this.configurationStage = (Stage)Cancel.getScene().getWindow();
        this.configurationStage.close();
    }

    @FXML
    public void handleClickArchive(ActionEvent event) {
        if(archived) {
            this.Archive.setText("Archiver");
            this.project.setArchived(false);
        } else {
            this.Archive.setText("Desarchiver");
            this.project.setArchived(true);
        }

        log.debug("Archive");
    }

    @FXML
    public void handleClickSave(ActionEvent event) {
        if(verifyIfChange()) {
            Boolean visibility = null;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information dialog");
            alert.setHeaderText(null);

            if (Public.isSelected())
                visibility = true;
            else if (Private.isSelected())
                visibility = false;

            this.setProject(Title.getText(), visibility, Module.getText(), NbMembers.getText(), FirstDay.getValue(), LastDay.getValue(), Description.getText());

            if (this.getProjectConfiguration().isComplete()) {
                if (this.getProjectConfiguration().isGoodLastDate()) {
                    this.updateProjectInformations(this.getProjectConfiguration(), this.project);
                } else {
                    alert.setContentText("La date de fin est avant la date de debut");
                    alert.showAndWait();
                }
            } else {
                alert.setContentText("Tous les champs * doivent être complets");
                alert.showAndWait();
            }
        }
    }

    private boolean verifyIfChange() {
        return changed && radioButtonChanged;
    }

    public ProjectConfiguration getProjectConfiguration() {
        return this.projectConfiguration;
    }

    private void setProject(String title, Boolean visibility, String module, String nbMembers, LocalDate firstDate,LocalDate lastDate, String description) {
        projectConfiguration = new ProjectConfiguration(title, visibility, module, nbMembers, firstDate, lastDate, description);
    }

    public void updateProjectInformations(ProjectConfiguration projectConfiguration, Project project) {
        project.setName(projectConfiguration.getTitle());
        project.setVisibility(projectConfiguration.getVisibility() ? Visibility.PUBLIC : Visibility.PRIVATE);
        project.setCreatedAt(Date.from(projectConfiguration.getFirstDay().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        project.setDescription(projectConfiguration.getDescription());
        projectConfiguration.saveInFile();
    }
}
