package eu.telecomnancy.pcd2k17;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Group;
import org.gitlab4j.api.models.Visibility;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class ConfigurationViewController implements Initializable {
    final static Logger log = LogManager.getLogger(ConfigurationViewController.class);

    private ArrayList<GroupConfiguration> groupsList;
    private GroupConfiguration groupConfiguration;
    private GitLabApi gitLab;
    private Group group;
    private Stage configurationStage;
    private boolean onGit = false;

    private int visibility;
    private int id;
    private String name;
    private String description;

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
    private Button AddMember;

    @FXML
    private Button AddCSV;

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
    private TextField FirstName;

    @FXML
    private TextField LastName;

    @FXML
    private TextField Mail;

    @FXML
    private DatePicker FirstDay;

    @FXML
    private DatePicker LastDay;

    @FXML
    private TextArea Description;

    @FXML
    private ImageView Avatar;

    @FXML
    private ChoiceBox<String> Promo;

    @FXML
    private ScrollPane MembersScroll;

    public ConfigurationViewController(GitLabApi gitLab, Group group) {
        this.gitLab = gitLab;
        this.group = group;
    }

    private final UnaryOperator<TextFormatter.Change> integerOnlyFilter = change -> {
        final String text = change.getText();
        return (text.isEmpty() || text.matches("[0-9]")) ? change : null;
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.initializeGroupConfiguration();
        this.initializeListenners();
    }

    private void initializeGroupConfiguration() {
        groupsList = GroupConfiguration.loadGroupsFromFile();
        int idProject = 0;

        if(groupsList != null) {
            for (GroupConfiguration g : groupsList) {
                if (g.getId() == group.getId()) {
                    onGit = true;
                    idProject = g.getId();
                    this.setGroupConfigurationFromId(idProject);
                    break;
                }
            }
        }

        id = this.group.getId();
        name = this.group.getName();
        visibility = this.group.getVisibility().toString().equals(Visibility.PUBLIC.toString()) ? 1 : 0;
        description = this.group.getDescription();

        if(idProject == 0) {
            this.setGroupConfiguration(id, name, visibility, "", "", null, null, description, new ArrayList<>(), false, null);
            groupConfiguration.saveGroupInFile(groupsList);
            groupsList = new ArrayList<>();
            groupsList.add(groupConfiguration);
        }

        this.initializePaneInfo();


        /*try {
            final TitledPane[] tps = new TitledPane[groups.size()];
            int i = 0;

            for (Group p : members) {
                tps[i]=new TitledPane(p.getName(), setContent(p));
                i++;
            }

            acc.getPanes().addAll(tps);
        } catch (GitLabApiException e) {
            e.printStackTrace();
        }*/
    }

    /*public Node setContent(MembersList membersList) {
        AnchorPane anchor = null;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ProjectContent.fxml"));
        loader.setControllerFactory(iC-> new MembersListController(group, gitLab));

        try {
            anchor = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return anchor;
    }*/

    private void initializePaneInfo() {
        this.Name.setText(name);
        this.Module.setText(this.groupConfiguration.getModule());
        this.NbMembers.setText(String.valueOf(this.groupConfiguration.getNbMembers()));
        this.FirstDay.setValue(this.groupConfiguration.getFirstDay());
        this.LastDay.setValue(this.groupConfiguration.getLastDay());
        this.Description.setText(description);
        this.Promo.getSelectionModel().select(this.groupConfiguration.getPromo());
        this.setArchiveText();

        try {
            Image image = SwingFXUtils.toFXImage(ImageIO.read(new URL(this.group.getAvatarUrl())), null);
            Avatar.setImage(image);
        } catch(IOException e) {
            e.printStackTrace();
        }

        if(visibility == 1)
            this.Public.setSelected(true);
        else this.Private.setSelected(true);
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
                if(oldValue.equals(newValue))
                    changed = true;
            }
        });

        Private.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(oldValue.equals(newValue))
                    changed = true;
            }
        });

        Promo.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                if(oldValue.equals(newValue))
                    changed = true;
            }
        });
    }

    @FXML
    public void handleClickAddFile(ActionEvent event) {
        /*final FileChooser fileChooser = new FileChooser();

        File file = fileChooser.showOpenDialog(configurationStage);
        if (file != null)
            openFile(file);*/

        log.debug("Add");
    }

    @FXML
    public void handleClickCancel(ActionEvent event) {
        if(!onGit) {
            try {
                gitLab.getGroupApi().deleteGroup(this.group);
            } catch (GitLabApiException e) {
                e.printStackTrace();
            }
        }

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
        if(this.changed || this.isDifferentFromGit()) {
            ArrayList members = new ArrayList();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information dialog");
            alert.setHeaderText(null);
            this.setGroupConfiguration(group.getId(), Name.getText(), Public.isSelected() ? 1 : 0, Module.getText(), NbMembers.getText(), FirstDay.getValue(), LastDay.getValue(), Description.getText(), members, !Archive.getText().equals("Archiver"), Promo.getSelectionModel().getSelectedItem());

            if(this.groupConfiguration.isComplete()) {
                if(this.groupConfiguration.isGoodLastDate()) {
                    this.updateGroupInformations(this.groupConfiguration);
                    this.configurationStage = (Stage)Save.getScene().getWindow();
                    this.configurationStage.close();
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

    private boolean isDifferentFromGit() {
        return this.id != this.groupConfiguration.getId()
                && this.Description.getText().equals(this.groupConfiguration.getDescription())
                && this.visibility == this.groupConfiguration.getVisibility();
    }

    private void setGroupConfiguration(int id, String name, int visibility, String module, String nbMembers, LocalDate firstDate, LocalDate lastDate, String description, ArrayList<MemberInformations> members, boolean archived, String promo) {
        this.groupConfiguration = new GroupConfiguration(id, name, visibility, module, nbMembers, firstDate, lastDate, description, members, archived, promo);
    }

    private void updateGroupInformations(GroupConfiguration groupConfiguration) {
        group.setName(groupConfiguration.getName());
        group.setPath(groupConfiguration.getName());
        group.setDescription(groupConfiguration.getDescription());
        groupConfiguration.saveGroupInFile(groupsList);

        if(groupConfiguration.getVisibility() == 1)
            group.setVisibility(Visibility.PUBLIC);
        else group.setVisibility(Visibility.PRIVATE);

        try {
            gitLab.getGroupApi().updateGroup(group.getId(), group.getName(), group.getPath(), group.getDescription(), group.getRequestAccessEnabled(), group.getRequestAccessEnabled(), group.getVisibility(), group.getRequestAccessEnabled(), group.getRequestAccessEnabled(), group.getParentId(), group.getSharedRunnersMinutesLimit());
        } catch (GitLabApiException e) {
            e.printStackTrace();
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Requête impossible");
            alertError.setHeaderText(null);
            alertError.setContentText("La sauvegarde n'a pas pu être effectué.\n\nVérifiez que le nom n'est pas déjà utilisé.");
            alertError.showAndWait();
        }
    }

    private void setGroupConfigurationFromId(int id) {
        this.groupConfiguration = GroupConfiguration.getById(id);
    }
}
