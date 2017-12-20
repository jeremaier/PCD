package eu.telecomnancy.pcd2k17;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class GroupConfiguration implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private int visibility;
    private String module;
    private int nbMembers;
    private LocalDate firstDay;
    private LocalDate lastDay;
    private String description;
    private ArrayList members;
    private boolean archived;

    public GroupConfiguration(int id, String name, int visibility, String module, String nbMembers, LocalDate firstDay, LocalDate lastDay, String description, ArrayList members, Boolean archived) {
        this.setId(id);
        this.setName(name);
        this.setVisibility(visibility);
        this.setLastDay(lastDay);
        this.setModule(module);
        this.setDescription(description);
        this.setMembers(members);

        if(nbMembers.isEmpty())
            this.setNbMembers(0);
        else this.setNbMembers(Integer.parseInt(nbMembers));

        if(firstDay == null)
            this.setFirstDay((new Date()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        else this.setFirstDay(firstDay);

        if(archived == null)
            this.setArchived(false);
        else this.setArchived(archived);
    }

    //Setter
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVisibility(int visibility) { this.visibility = visibility; }

    public void setModule(String module) { this.module = module; }

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

    public void setMembers(ArrayList members) {
        this.members = members;
    }

    public void setArchived(boolean archived) { this.archived = archived; }

    //Getter
    public int getId() { return this.id; }

    public String getName() { return this.name; }

    public int getVisibility() { return visibility; }

    public String getModule() { return this.module; }

    public int getNbMembers() { return this.nbMembers; }

    public LocalDate getFirstDay() { return this.firstDay; }

    public LocalDate getLastDay() { return this.lastDay; }

    public String getDescription() { return this.description; }

    public ArrayList getMembers() { return this.members; }

    public boolean getArchived() { return this.archived; }

    public boolean isComplete() {
        if(this.getName().isEmpty() || this.getNbMembers() == 0 || this.getLastDay() == null)
            return false;
        else return true;
    }

    public boolean isGoodLastDate() {
        if(this.getLastDay().isAfter(this.getFirstDay()) || this.getLastDay().isEqual(this.getFirstDay()))
            return true;
        else return false;
    }

    public static String getFilePath() {
        String directoryPath = System.getProperty("user.dir") + "\\saves";
        File folder = new File(directoryPath);

        if(!folder.exists())
            folder.mkdirs();

        return folder.getAbsoluteFile() + File.separator + "Projects";
    }

    public void saveProjectsInFile() {
        FileOutputStream fOut;
        ObjectOutputStream oOut;

        List<GroupConfiguration> projectConfigurationListAncient = this.loadProjectsFromFile();
        ArrayList projectConfigurationListRecent = new ArrayList();

        if (projectConfigurationListAncient != null)
            for (int i = 0; i < projectConfigurationListAncient.size(); i++)
                if (this.id != projectConfigurationListAncient.get(i).getId())
                    projectConfigurationListRecent.add(projectConfigurationListAncient.get(i));

        projectConfigurationListRecent.add(this);

        try {
            fOut = new FileOutputStream(getFilePath());
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

    public static ArrayList<GroupConfiguration> loadProjectsFromFile() {
        File file = new File(getFilePath());
        FileInputStream fIn;
        ObjectInputStream oIn;

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(file.length() > 0) {
            try {
                fIn = new FileInputStream(file);
                oIn = new ObjectInputStream(fIn);

                ArrayList projectConfiguration = (ArrayList) oIn.readObject();

                if (oIn != null)
                    oIn.close();

                if (fIn != null)
                    fIn.close();

                return projectConfiguration;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static GroupConfiguration getById(int id) {
        List<GroupConfiguration> projectConfigurationList = loadProjectsFromFile();

        for (int i = 0; i < loadProjectsFromFile().size(); i++) {
            GroupConfiguration projectConfiguration = projectConfigurationList.get(i);

            if (projectConfiguration.getId() == id)
                return projectConfiguration;
        }

        return null;
    }
}
