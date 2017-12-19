package eu.telecomnancy.pcd2k17;

public class Group {

    public int id;
    public String name;
    public int number;
    public String[] members;

    public Group(int id, String name, int number) {
        this.id = id;
        this.name = name;
        this.number = 0;
        this.members = new String[10];
    }

    public Group(int id, String name, int nombre, String[] members) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.members = members;
    }

    public void addMember(String member) {
        this.members[number] = member;
        this.number++;
    }

    public void removeMember(String member) {
        boolean bool = false;
        for (int i=0;i<this.number;i++) {
            if (members[i].equals(member)) {
                members[i] = null;
                bool = true;
            }
            if (bool) {
                members[i] = members[i+1];
            }
        }
        members[this.number] = null;
        this.number--;
    }

    public String getMember(int i) {
        return this.members[i];
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