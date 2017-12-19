package eu.telecomnancy.pcd2k17;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class CSVListener {
    private ArrayList<Member> members = new ArrayList();

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

            while(scan.hasNextLine()) {
                scan.useDelimiter("\\s*;\\s*");
                String lastName = scan.next();
                scan.next();
                String firstName = scan.next();
                scan.next();
                String mail = scan.next();

                members.add(new Member(lastName, firstName, mail));
                scan.nextLine();
            }

            scan.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Member> extract() {
        return this.members;
    }
}
