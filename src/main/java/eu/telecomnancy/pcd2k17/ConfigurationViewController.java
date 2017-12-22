package eu.telecomnancy.pcd2k17;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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
    private ArrayList<MemberInformations> members = new ArrayList<>();

    private int visibility;
    private int id;
    private String name;
    private String description;

    private boolean changed = false;
    private ArrayList<Button> removeButtons = new ArrayList<>();

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
    private Button RemoveAll;

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
    private GridPane MembersList;

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
        groupsList = FileManager.loadGroupsFromFile();
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

        if(idProject == 0)
            this.groupConfiguration = new GroupConfiguration(id, name, visibility, "", "", null, null, description, new ArrayList<>(), false, null);

        this.initializePaneInfo();
    }

    private void addMemberToList() {
        String[] fields = {this.LastName.getText(), this.FirstName.getText(), this.Mail.getText()};
        boolean allInformations = true;
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning dialog");
        alert.setHeaderText(null);

        for (String field : fields) {
            field = field.trim();

            if ((field.isEmpty()) || (field.contains(" "))) {
                allInformations = false;
                break;
            }
        }

        if(!fields[2].contains("@"))
            allInformations = false;

        if(allInformations) {
            MemberInformations member = new MemberInformations(fields[0].toLowerCase(), fields[1].toLowerCase(), fields[2].toLowerCase());
            boolean contains = false;
            int length = members.size();

            for (int i = 0; i < length; i++)
                if ((members.get(i).getFirstname().equals(member.getFirstname()) && members.get(i).getLastName().equals(member.getLastName())) || members.get(i).getEmail().equals(member.getEmail()))
                    contains = true;

            if(!contains) {
                this.members.add(member);
                this.addRow(members.size() - 1, members);
                this.FirstName.setText("");
                this.LastName.setText("");
                this.Mail.setText("");
                this.FirstName.setPromptText("Prénom");
                this.LastName.setPromptText("Nom");
                this.Mail.setPromptText("Email");
            } else {
                alert.setContentText("Le membre existe déjà dans la liste");
                alert.showAndWait();
            }
        } else {
            alert.setContentText("Les informations nécessaires ne sont pas valides");
            alert.showAndWait();
        }
    }

    private void reDoGridPane() {
        Node node = this.MembersList.getChildren().get(0);
        this.MembersList.getChildren().clear();
        this.MembersList.getChildren().add(0, node);

        /*if(this.MembersList != null) {
            for (int i = 0; i < this.members.size(); i++)
                addRow(members.size() - 1, members);
        }*/
    }

    private void addRow(int pos, ArrayList<MemberInformations> members) {
        MemberInformations member = members.get(pos);
        Label label = new Label(member.getLastName().toUpperCase() + " " + member.getFirstname().toLowerCase() + " <" + member.getEmail() + ">");
        MembersScroll.setVvalue(1.0);
        removeButtons.add(new Button("X"));
        removeButtons.get(pos).setOnAction(e -> removeMember(pos));
        this.MembersList.add(label, 0, pos);
        this.MembersList.add(removeButtons.get(pos), 1, pos);
    }

    private void removeMember(int i) {
        this.members.remove(i);
        this.removeButtons.remove(i);
        reDoGridPane();
    }

    private void removeAllMembers() {
        members = new ArrayList<>();
        removeButtons = new ArrayList<>();
        MembersScroll.setVvalue(0.0);
        reDoGridPane();
    }

    private void addFromCSV(String filePath) {
        ArrayList<MemberInformations> membersCSV = new CSVListener(filePath).extract();
        ArrayList<MemberInformations> ancientMembers = new ArrayList<>();
        int countAlreadyAdd = 0;
        int posMax = 0;

        if(!membersCSV.isEmpty()) {
            boolean contains = false;

            for (int i = 0; i < membersCSV.size(); i++) {
                for (int j = 0; j < members.size(); j++)
                    if ((members.get(j).getFirstname().equals(membersCSV.get(i).getFirstname()) && members.get(j).getLastName().equals(membersCSV.get(i).getLastName())) || members.get(i).getEmail().equals(membersCSV.get(i).getEmail()))
                        contains = true;

                if (!contains) {
                    ancientMembers.add(membersCSV.get(i));
                    addRow(posMax, ancientMembers);
                    posMax++;
                } else countAlreadyAdd++;
            }
        }

        if(countAlreadyAdd > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning dialog");
            alert.setHeaderText(null);
            alert.setContentText(countAlreadyAdd + " membre(s) existai(en)t déjà dans la liste");
            alert.showAndWait();
        }

        members = ancientMembers;
    }

    private void setMembersList() {
        ArrayList<MemberInformations> groupConfMembersList = this.groupConfiguration.getMembersList();
        int length = groupConfMembersList.size();

        if(length != 0) {
            for (int i = 0; i < length; i++) {
                this.members.add(groupConfMembersList.get(i));
                this.addRow(members.size() - 1, members);
            }
        }
    }

    private void initializePaneInfo() {
        this.Name.setText(name);
        this.Module.setText(this.groupConfiguration.getModule());
        this.NbMembers.setText(String.valueOf(this.groupConfiguration.getNbMembers()));
        this.FirstDay.setValue(this.groupConfiguration.getFirstDay());
        this.LastDay.setValue(this.groupConfiguration.getLastDay());
        this.Description.setText(description);
        this.Promo.getSelectionModel().select(this.groupConfiguration.getPromo());
        this.setArchiveText();
        this.setMembersList();

        String avatarUrl = this.group.getAvatarUrl();

        if(avatarUrl != null) {
            try {
                Image image = SwingFXUtils.toFXImage(ImageIO.read(new URL(avatarUrl)), null);
                Avatar.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        log.debug("Add");
    }

    @FXML
    public void handleClickAddMember(ActionEvent event) {
        this.addMemberToList();
        log.debug("AddMember");
    }

    @FXML
    public void handleClickUseCSV(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouverture d'un fichier .csv");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichiers csv", "*.csv"));

        File file = fileChooser.showOpenDialog(configurationStage);

        if (file != null)
            this.addFromCSV(file.getAbsolutePath());

        log.debug("UseCSV");
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
    public void handleClickRemoveAll(ActionEvent event) {
        this.removeAllMembers();
    }

    @FXML
    public void handleClickSave(ActionEvent event) {
        if(this.changed || this.isDifferentFromGit() || !this.members.equals(this.groupConfiguration.getMembersList())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information dialog");
            alert.setHeaderText(null);
            this.groupConfiguration = new GroupConfiguration(group.getId(), Name.getText(), Public.isSelected() ? 1 : 0, Module.getText(), NbMembers.getText(), FirstDay.getValue(), LastDay.getValue(), Description.getText(), members, !Archive.getText().equals("Archiver"), Promo.getSelectionModel().getSelectedItem());

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
                && this.visibility == this.groupConfiguration.getVisibility()
                && this.members != this.groupConfiguration.getMembersList();
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
