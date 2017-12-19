package eu.telecomnancy.pcd2k17;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.Visibility;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class ConfigurationViewController implements Initializable {
    final static Logger log = LogManager.getLogger(ConfigurationViewController.class);

    private ProjectConfiguration projectConfiguration;
    private Project project;
    Stage configurationStage;

    private boolean archived = false;
    private boolean changed = false;

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
    private TextField Name;

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
        ArrayList<ProjectConfiguration> projectslist = ProjectConfiguration.loadProjectsFromFile();
        int idProject = 0;

        if(projectslist != null) {
            for (ProjectConfiguration p : projectslist) {
                if (p.getId() == project.getId()) {
                    idProject = p.getId();
                    this.setProjectConfigurationFromId(idProject);
                    break;
                }
            }
        }

        if(idProject == 0) {
            this.setProjectConfiguration(this.project.getId(), this.project.getName(), 0, "", "", null, null, "", new ArrayList());
            this.Name.setText(this.projectConfiguration.getName());
            projectConfiguration.saveProjectsInFile();
        } else this.initializePaneInfo();

        this.initializeListenners();
    }

    private void initializePaneInfo() {
        archived = this.project.getArchived();
        int visibility = this.projectConfiguration.getVisibility();
        this.Name.setText(this.projectConfiguration.getName());
        this.Module.setText(this.projectConfiguration.getModule());
        this.NbMembers.setText(String.valueOf(this.projectConfiguration.getNbMembers()));
        this.FirstDay.setValue(this.projectConfiguration.getFirstDay());
        this.LastDay.setValue(this.projectConfiguration.getLastDay());
        this.Description.setText(this.projectConfiguration.getDescription());

        if(visibility == 1)
            this.Private.setSelected(true);
        else this.Public.setSelected(true);

        if(archived)
            Archive.setText("Desarchiver");
        else Archive.setText("Archiver");
    }

    private void initializeListenners() {
        final BooleanProperty firstTime = new SimpleBooleanProperty(true);

        NbMembers.setTextFormatter(new TextFormatter(integerOnlyFilter));
        Name.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue && firstTime.get()) {
                controllerView.requestFocus();
                firstTime.setValue(false);
            }
        });

        Name.lengthProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                changed = true;

                System.out.println(Name.getText().length());

                if(newValue.intValue() > oldValue.intValue())
                    if(Name.getText().contains(" "))
                        Name.setText(Name.getText().substring(0, Name.getText().length() - 1));
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
    public void handleClickAddFile(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();

        File file = fileChooser.showOpenDialog(configurationStage);
        /*if (file != null)
            openFile(file);*/

        log.debug("Add");
    }

    @FXML
    public void handleClickCancel(ActionEvent event) {
        this.configurationStage = (Stage)Cancel.getScene().getWindow();
        this.configurationStage.close();
    }

    @FXML
    public void handleClickArchive(ActionEvent event) {
        if(archived) {
            archived = false;
            this.Archive.setText("Archiver");
            this.project.setArchived(false);
        } else {
            archived = true;
            this.Archive.setText("Desarchiver");
            this.project.setArchived(true);
        }
    }

    @FXML
    public void handleClickSave(ActionEvent event) {
        if(this.changed) {
            ArrayList members = new ArrayList();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information dialog");
            alert.setHeaderText(null);

            this.setProjectConfiguration(project.getId(), Name.getText(), Public.isSelected() ? 1 : 0, Module.getText(), NbMembers.getText(), FirstDay.getValue(), LastDay.getValue(), Description.getText(), members);

            if(this.getProjectConfiguration().isComplete()) {
                if(this.getProjectConfiguration().isGoodLastDate()) {
                    this.updateProjectInformations(this.getProjectConfiguration(), this.project);
                    alert.setContentText("Le projet a bien été sauvegardé");
                    alert.showAndWait();
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

    private ProjectConfiguration getProjectConfiguration() {
        return this.projectConfiguration;
    }

    private void setProjectConfiguration(int id, String name, int visibility, String module, String nbMembers, LocalDate firstDate, LocalDate lastDate, String description, ArrayList members) {
        this.projectConfiguration = new ProjectConfiguration(id, name, visibility, module, nbMembers, firstDate, lastDate, description, members);
    }

    private void updateProjectInformations(ProjectConfiguration projectConfiguration, Project project) {
        project.setName(projectConfiguration.getName());
        project.setCreatedAt(Date.from(projectConfiguration.getFirstDay().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        project.setDescription(projectConfiguration.getDescription());
        projectConfiguration.saveProjectsInFile();

        if(projectConfiguration.getVisibility() == 1)
            project.setVisibility(Visibility.PUBLIC);
        else project.setVisibility(Visibility.PRIVATE);
    }

    private void setProjectConfigurationFromId(int id) {
        this.projectConfiguration = ProjectConfiguration.getById(id);
    }
}
