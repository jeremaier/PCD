package eu.telecomnancy.pcd2k17;

import org.gitlab4j.api.models.AccessLevel;
import org.gitlab4j.api.models.Member;

import java.io.Serializable;

public class MemberInformations extends Member implements Serializable {
    private String lastName;
    private String firstName;
    private String email;

    public MemberInformations(String lastName, String firstName, String email) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getFirstname() {
        return this.firstName;
    }

    public String getEmail() { return this.email; }

    @Override
    public void setEmail(String email) { this.email = email;}

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public void setMasterAccess() { this.setAccessLevel(AccessLevel.MASTER); }

    public void setGuestAccess() { this.setAccessLevel(AccessLevel.GUEST); }
}