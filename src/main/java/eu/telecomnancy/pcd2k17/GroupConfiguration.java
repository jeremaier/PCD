package eu.telecomnancy.pcd2k17;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import org.gitlab4j.api.models.Group;

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
    private ArrayList<MemberInformations> membersList;
    private boolean archived;
    private String promo;

    public GroupConfiguration(int id, String name, int visibility, String module, String nbMembers, LocalDate firstDay, LocalDate lastDay, String description, ArrayList<MemberInformations> membersList, boolean archived, String promo) {
        this.setId(id);
        this.setName(name);
        this.setVisibility(visibility);
        this.setLastDay(lastDay);
        this.setModule(module);
        this.setDescription(description);
        this.setMembers(membersList);
        this.setArchived(archived);
        this.setPromo(promo);

        if(nbMembers.isEmpty())
            this.setNbMembers(0);
        else this.setNbMembers(Integer.parseInt(nbMembers));

        if(firstDay == null)
            this.setFirstDay((new Date()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        else this.setFirstDay(firstDay);
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

    public void setNbMembers(int nbMembers) { this.nbMembers = nbMembers; }

    public void setFirstDay(LocalDate firstDay) {
        this.firstDay = firstDay;
    }

    public void setLastDay(LocalDate lastDay) {
        this.lastDay = lastDay;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMembers(ArrayList<MemberInformations> membersList) { this.membersList = membersList; }

    public void setArchived(boolean archived) { this.archived = archived; }

    public void setPromo(String promo) { this.promo = promo; }

    //Getter
    public int getId() { return this.id; }

    public String getName() { return this.name; }

    public int getVisibility() { return visibility; }

    public String getModule() { return this.module; }

    public int getNbMembers() { return this.nbMembers; }

    public LocalDate getFirstDay() { return this.firstDay; }

    public LocalDate getLastDay() { return this.lastDay; }

    public String getDescription() { return this.description; }

    public ArrayList<MemberInformations> getMembersList() { return this.membersList; }

    public boolean getArchived() { return this.archived; }

    public String getPromo() { return this.promo; }

    public void addMember(MemberInformations member) { membersList.add(member); }

    public void removeMember(MemberInformations member) { membersList.remove(member); }

    public boolean isComplete() {
        if(this.getName().isEmpty() || this.getNbMembers() == 0 || this.getLastDay() == null || this.getPromo() == null)
            return false;
        else return true;
    }

    public boolean isGoodLastDate() {
        if(this.getLastDay().isAfter(this.getFirstDay()) || this.getLastDay().isEqual(this.getFirstDay()))
            return true;
        else return false;
    }

    public static String getFilePath() {
        String directoryPath = System.getProperty("user.dir") + File.separator + "saves";
        File folder = new File(directoryPath);

        if(!folder.exists())
            folder.mkdirs();

        return folder.getAbsoluteFile() + File.separator + "Groups";
    }

    public static ArrayList<GroupConfiguration> loadGroupsFromFile() {
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

                ArrayList groupsConfiguration = (ArrayList) oIn.readObject();

                if (oIn != null)
                    oIn.close();

                if (fIn != null)
                    fIn.close();

                return groupsConfiguration;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void saveGroupInFile(ArrayList<GroupConfiguration> ancientGroupsList) {
        ArrayList groupsConfigurationListRecent = new ArrayList();

        if (ancientGroupsList != null)
            for (int i = 0; i < ancientGroupsList.size(); i++)
                if (this.id != ancientGroupsList.get(i).getId())
                    groupsConfigurationListRecent.add(ancientGroupsList.get(i));

        groupsConfigurationListRecent.add(this);

        saveGroupsInFile(groupsConfigurationListRecent);
    }

    public static GroupConfiguration getById(int id) {
        List<GroupConfiguration> groupsConfigurationList = loadGroupsFromFile();

        if(groupsConfigurationList != null) {
            for (int i = 0; i < groupsConfigurationList.size(); i++) {
                GroupConfiguration groupsConfiguration = groupsConfigurationList.get(i);

                if (groupsConfiguration.getId() == id)
                    return groupsConfiguration;
            }
        }

        return null;
    }

    public static ArrayList getNewGroupsInGit(List<Group> groupsList) {
        List<GroupConfiguration> groupsConfigurationList = loadGroupsFromFile();
        ArrayList newGroupsList = new ArrayList();
        int k;

        if (groupsList != null) {
            for (int i = 0; i < groupsList.size(); i++) {
                k = -1;

                for (int j = 0; j < groupsConfigurationList.size(); j++) {
                    if (groupsList.get(i).getId() == groupsConfigurationList.get(j).getId()) {
                        k = j;
                        break;
                    }
                }

                if(k >= 0)
                    newGroupsList.add(groupsConfigurationList.get(k));
                else newGroupsList.add(groupsList.get(i));
            }

            return newGroupsList;
        }

        return null;
    }

    public static void saveGroupsInFile(ArrayList<GroupConfiguration> groupsList) {
        if(groupsList != null) {
            FileOutputStream fOut;
            ObjectOutputStream oOut;

            try {
                fOut = new FileOutputStream(getFilePath());
                oOut = new ObjectOutputStream(fOut);

                oOut.writeObject(groupsList);

                if (oOut != null)
                    oOut.close();

                if (fOut != null)
                    fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
