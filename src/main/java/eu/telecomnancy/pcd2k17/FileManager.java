package eu.telecomnancy.pcd2k17;

import org.gitlab4j.api.models.Group;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public static String getFilePath(String name) {
        String directoryPath = System.getProperty("user.dir") + File.separator + "saves";
        File folder = new File(directoryPath);

        if(!folder.exists())
            folder.mkdirs();

        return folder.getAbsoluteFile() + File.separator + name;
    }

    public static ArrayList<GroupConfiguration> loadGroupsFromFile() {
        File file = new File(getFilePath("groups"));
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
                fOut = new FileOutputStream(getFilePath("groups"));
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