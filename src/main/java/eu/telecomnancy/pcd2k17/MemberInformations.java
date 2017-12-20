package eu.telecomnancy.pcd2k17;

import org.gitlab4j.api.models.AccessLevel;
import org.gitlab4j.api.models.Member;

public class MemberInformations extends Member {
    private String lastName;
    private String firstName;

    public MemberInformations(String lastName, String firstName, String email) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.setMasterAccess();
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getFirstname() {
        return this.firstName;
    }

    public void setAccessLevel() { this.setAccessLevel(AccessLevel.MASTER); }

   // public void setFirstName() { this.; }

    public void setMasterAccess() { this.setAccessLevel(AccessLevel.MASTER); }

    public void setGuestAccess() { this.setAccessLevel(AccessLevel.MASTER); }
}