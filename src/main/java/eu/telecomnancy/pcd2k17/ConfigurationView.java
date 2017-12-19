package eu.telecomnancy.pcd2k17;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gitlab4j.api.GitLabApi;

import java.time.LocalDate;

public class ConfigurationView {

    final static Logger log = LogManager.getLogger(ConfigurationView.class);
    private static Project project;
    private static GitLabApi gitLab;

    public void startConfigurationView(GitLabApi gitLab/*, ProjectView projectView*/) {
        this.gitLab = gitLab;
        log.debug("executing ConfigurationView method.");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("configurationView.fxml"));
    }

    public static Project getProject() {
        return project;
    }

    public GitLabApi getGitLab() {
        return this.gitLab;
    }

    public static void setProject(String title, String module, String nbMembers, LocalDate firstDate,LocalDate lastDate, String description) {
        project = new Project(title, module, nbMembers, firstDate, lastDate, description);
    }
}
