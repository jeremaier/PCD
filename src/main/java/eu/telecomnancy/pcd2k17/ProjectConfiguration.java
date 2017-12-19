package eu.telecomnancy.pcd2k17;

import java.io.*;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class ProjectConfiguration implements Serializable {
    // Constant
    private static final long serialVersionUID = 1L;

    private int id;
    private String title;
    private Boolean visibility;
    private String module;
    private int nbMembers;
    private LocalDate firstDay;
    private LocalDate lastDay;
    private String description;

    public ProjectConfiguration(String title, Boolean visibility, String module, String nbMembers, LocalDate firstDay, LocalDate lastDay, String description) {
        this.title = title;
        this.visibility = visibility;
        this.lastDay = lastDay;
        this.module = module;
        this.description = description;

        if(nbMembers.equals(""))
            this.nbMembers = 0;
        else this.nbMembers = Integer.parseInt(nbMembers);

        if(firstDay == null) {
            Date date = new Date();

            this.firstDay = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } else this.firstDay = firstDay;
    }

    //Setter
    public void setTitle(String title) {
        this.title = title;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setNbMembers(int nbMembers) {
        this.nbMembers = nbMembers;
    }

    public void setFirstDay(LocalDate firstDay) {
        this.firstDay = firstDay;
    }

    public void setLastDay(LocalDate lastDay) {
        this.lastDay = lastDay;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //Getter
    public String getTitle() { return this.title; }

    public Boolean getVisibility() { return visibility; }

    public String getModule() { return this.module; }

    public int getNbMembers() { return this.nbMembers; }

    public LocalDate getFirstDay() { return this.firstDay; }

    public LocalDate getLastDay() { return this.lastDay; }

    public String getDescription() { return this.description; }

    public boolean isComplete() {
        if(this.getTitle().equals("") || this.getVisibility() == null || this.getNbMembers() == 0 || this.getLastDay() == null)
            return true;
        else return false;
    }

    public boolean isGoodLastDate() {
        if(this.getLastDay().isAfter(this.getFirstDay()))
            return true;
        else return false;
    }

    public void saveInFile() {
        String directoryPath = System.getProperty("user.dir") + "\\Saves";
        File folder = new File(directoryPath);

        if(!folder.exists())
            folder.mkdirs();

        FileOutputStream fOut;
        ObjectOutputStream oOut;

        List<ProjectConfiguration> projectConfigurationListAncient = this.loadFromFile();
        List<ProjectConfiguration> projectConfigurationListRecent = new ArrayList();

        for(int i = 0; i < projectConfigurationListAncient.size(); i++)
            if(this.title != projectConfigurationListAncient.get(i).getTitle())
                projectConfigurationListRecent.add(projectConfigurationListAncient.get(i));

        projectConfigurationListRecent.add(this);

        try {
            fOut = new FileOutputStream(directoryPath + "\\Projects");
            oOut = new ObjectOutputStream(fOut);

            oOut.writeObject(projectConfigurationListRecent);

            if(oOut != null)
                oOut.close();

            if(fOut != null)
                fOut.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ProjectConfiguration> loadFromFile() {
        File file = new File(System.getProperty("user.dir") + "\\Saves\\Projects");
        FileInputStream fIn;
        ObjectInputStream oIn;

        try {
            fIn = new FileInputStream(file);
            oIn = new ObjectInputStream(fIn);

            ArrayList projectConfiguration = (ArrayList)oIn.readObject();

            if(oIn != null)
                oIn.close();

            if(fIn != null)
                fIn.close();

            return projectConfiguration;
        } catch(IOException e) {
            e.printStackTrace();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
