package eu.telecomnancy.pcd2k17;

public class Member {
    private String lastName;
    private String firstName;
    private String email;

    public Member(String lastName, String firstName, String email) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getFirstname() {
        return this.firstName;
    }
}
