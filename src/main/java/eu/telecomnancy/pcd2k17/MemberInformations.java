package eu.telecomnancy.pcd2k17;

import org.gitlab4j.api.models.AccessLevel;
import org.gitlab4j.api.models.Member;

import java.io.Serializable;

public class MemberInformations extends Member implements Serializable {
    private String lastName;
    private String firstName;
    private int projectId;

    public MemberInformations(String lastName, String firstName, String email, int projectId) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
        this.setProjectId(projectId);
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getFirstname() {
        return this.firstName;
    }

    public int getProjectId() { return projectId; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public void setProjectId(int projectId) { this.projectId = projectId; }

    public void setMasterAccess() { this.setAccessLevel(AccessLevel.MASTER); }

    public void setGuestAccess() { this.setAccessLevel(AccessLevel.GUEST); }
}