package eu.telecomnancy.pcd2k17;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Project {
    private final StringProperty title;
    private final BooleanProperty visibility;
    private final StringProperty module;
    private final IntegerProperty nbMembers;
    private final ObjectProperty<LocalDate> firstDay;
    private final ObjectProperty<LocalDate> lastDay;
    private final StringProperty description;

    public Project(String title, String module, String nbMembers, LocalDate firstDay, LocalDate lastDay, String description) {
        this.title = new SimpleStringProperty(title);
        this.lastDay = new SimpleObjectProperty<>(lastDay);
        this.module = new SimpleStringProperty(module);
        this.description = new SimpleStringProperty(description);

        if(firstDay == null) {
            Date date = new Date();

            this.firstDay = new SimpleObjectProperty<>(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else this.firstDay = new SimpleObjectProperty<>(firstDay);

        if(!nbMembers.equals(""))
            this.nbMembers = new SimpleIntegerProperty(Integer.getInteger(nbMembers));
        else this.nbMembers = new SimpleIntegerProperty(0);
    }

    //Setter
    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setModule(String module) {
        this.module.set(module);
    }

    public void setNbMembers(int nbMembers) {
        this.nbMembers.set(nbMembers);
    }

    public void setFirstDay(LocalDate firstDay) {
        this.firstDay.set(firstDay);
    }

    public void setLastDay(LocalDate lastDay) {
        this.lastDay.set(lastDay);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    //Property
    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty moduleProperty() {
        return module;
    }

    public IntegerProperty nbMembersProperty() {
        return nbMembers;
    }

    public ObjectProperty<LocalDate> firstDayProperty() {
        return firstDay;
    }

    public ObjectProperty<LocalDate> lastDayProperty() {
        return lastDay;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public boolean isComplete() {
        if(!titleProperty().get().equals("") && nbMembersProperty().get() != 0 && !lastDayProperty().get().equals(null))
            return false;

        return true;
    }
}
