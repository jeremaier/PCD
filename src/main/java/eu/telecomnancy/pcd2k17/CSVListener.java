package eu.telecomnancy.pcd2k17;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class CSVListener {
    private ArrayList<MemberInformations> members = new ArrayList();

    public CSVListener(String nomFichier) {
        load(nomFichier);
    }

    public void load(String nomFichier) {
        FileReader flot ;
        BufferedReader flotFiltre;

        try {
            flot = new FileReader(nomFichier);
            flotFiltre = new BufferedReader(flot);
            Scanner scan = new Scanner(flotFiltre);
            scan.useLocale(Locale.ENGLISH);
            scan.useDelimiter("\\s*;\\s*");

            while(scan.hasNext()) {
                String firstName = scan.next();
                String lastName = scan.next();
                String mail = scan.next();

                members.add(new MemberInformations(lastName, firstName, mail));
            }

            scan.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<MemberInformations> extract() {
        return this.members;
    }
}