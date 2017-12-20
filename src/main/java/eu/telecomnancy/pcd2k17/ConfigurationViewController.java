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
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Group;
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

    private GroupConfiguration groupConfiguration;
    private GitLabApi gitLab;
    private Group group;
    private Stage configurationStage;

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

    public ConfigurationViewController(GitLabApi gitLab, Group group) {
        this.gitLab = gitLab;
        this.group = group;
    }

    public final UnaryOperator<TextFormatter.Change> integerOnlyFilter = change -> {
        final String text = change.getText();
        return (text.isEmpty() || text.matches("[0-9]")) ? change : null;
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.initializeGroupConfiguration();
        this.initializeListenners();
    }

    private void initializeGroupConfiguration() {
        ArrayList<GroupConfiguration> projectslist = GroupConfiguration.loadProjectsFromFile();
        int idProject = 0;

        if(projectslist != null) {
            for (GroupConfiguration p : projectslist) {
                if (p.getId() == group.getId()) {
                    idProject = p.getId();
                    this.setGroupConfigurationFromId(idProject);
                    break;
                }
            }
        }

        if(idProject == 0) {
            this.setGroupConfiguration(this.group.getId(), this.group.getName(), 0, "", "", null, null, "", new ArrayList(), false);
            this.Name.setText(this.groupConfiguration.getName());
            this.setArchiveText();
            groupConfiguration.saveProjectsInFile();
        } else this.initializePaneInfo();
    }

    private void initializePaneInfo() {
        int visibility = this.groupConfiguration.getVisibility();
        this.Name.setText(this.groupConfiguration.getName());
        this.Module.setText(this.groupConfiguration.getModule());
        this.NbMembers.setText(String.valueOf(this.groupConfiguration.getNbMembers()));
        this.FirstDay.setValue(this.groupConfiguration.getFirstDay());
        this.LastDay.setValue(this.groupConfiguration.getLastDay());
        this.Description.setText(this.groupConfiguration.getDescription());
        this.setArchiveText();

        if(visibility == 0)
            this.Private.setSelected(true);
        else this.Public.setSelected(true);
    }

    private void setArchiveText() {
        if(this.groupConfiguration.getArchived())
            this.Archive.setText("Desarchiver");
        else this.Archive.setText("Archiver");
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

        Public.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(oldValue != newValue)
                    changed = true;
            }
        });

        Private.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(oldValue != newValue)
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
        boolean changeArchivedTo = !this.groupConfiguration.getArchived();
        this.groupConfiguration.setArchived(changeArchivedTo);
        this.setArchiveText();
    }

    @FXML
    public void handleClickSave(ActionEvent event) {
        if(this.changed) {
            ArrayList members = new ArrayList();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information dialog");
            alert.setHeaderText(null);

            this.setGroupConfiguration(group.getId(), Name.getText(), Public.isSelected() ? 1 : 0, Module.getText(), NbMembers.getText(), FirstDay.getValue(), LastDay.getValue(), Description.getText(), members, !Archive.getText().equals("Archiver"));

            if(this.getGroupConfiguration().isComplete()) {
                if(this.getGroupConfiguration().isGoodLastDate()) {
                    this.updateProjectInformations(this.getGroupConfiguration(), alert);
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

    private GroupConfiguration getGroupConfiguration() {
        return this.groupConfiguration;
    }

    private void setGroupConfiguration(int id, String name, int visibility, String module, String nbMembers, LocalDate firstDate, LocalDate lastDate, String description, ArrayList members, boolean archived) {
        this.groupConfiguration = new GroupConfiguration(id, name, visibility, module, nbMembers, firstDate, lastDate, description, members, archived);
    }

    private void updateProjectInformations(GroupConfiguration groupConfiguration, Alert alert) {
        group.setName(groupConfiguration.getName());
        group.setDescription(groupConfiguration.getDescription());
        groupConfiguration.saveProjectsInFile();

        if(groupConfiguration.getVisibility() == 0)
            group.setVisibility(Visibility.PRIVATE);
        else group.setVisibility(Visibility.PUBLIC);

        /*try {
            Project projectResponse = gitLab.getProjectApi().updateProject(group);

            if(projectResponse != null) {
                alert.setContentText("Le projet a bien été sauvegardé");
                alert.showAndWait();
            }
        } catch (GitLabApiException e) {
            e.printStackTrace();
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Error dialog");
            alertError.setHeaderText(null);
            alertError.setContentText("La sauvegarde n'a pas pu être effectué");
            alertError.showAndWait();
        }*/
    }

    private void setGroupConfigurationFromId(int id) {
        this.groupConfiguration = GroupConfiguration.getById(id);
    }
}
