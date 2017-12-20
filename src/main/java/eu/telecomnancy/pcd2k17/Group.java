package eu.telecomnancy.pcd2k17;

import java.util.ArrayList;

public class Group {

    public int id;
    public String name;
    public int number;
    public ArrayList<MemberInformations> members;

    public Group(int id, String name, int number) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.members = new ArrayList<MemberInformations>();
    }

    public Group(int id, String name, ArrayList<MemberInformations> members) {
        this.id = id;
        this.name = name;
        this.members = members;
        this.number = members.size();
    }

    public void addMember(MemberInformations member) {
        this.members.add(member);
    }

    public void removeMember(int pos) {
        this.members.remove(pos);
    }

    public String getMember(int i) {
        return this.members.get(i).getLastName();
    }

    public int getNumber() {
        return this.members.size();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return this.id;
    }

    public String getMembers() {
        String res = "(";
        for (int i=0;i<this.number;i++) {
            res += this.getMember(i);
            if (i!=this.number-1) {
                res+=", ";
            }
        }
        res+=(")");
        return res;
    }
}