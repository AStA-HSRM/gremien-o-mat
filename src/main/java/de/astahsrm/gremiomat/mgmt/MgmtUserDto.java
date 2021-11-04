package de.astahsrm.gremiomat.mgmt;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import de.astahsrm.gremiomat.gremium.Gremium;

public class MgmtUserDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @NotBlank
    private String role;

    private Gremium gremien;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Gremium getGremien() {
        return gremien;
    }

    public void setGremien(Gremium gremien) {
        this.gremien = gremien;
    }

}
