package de.astahsrm.gremiomat.security;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import  javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import de.astahsrm.gremiomat.candidate.Candidate;

@Entity
public class MgmtUser {
    @Id
    @NotBlank 
    private String username;
    @NotBlank 
    private String password;
    @NotBlank 
    private String role;
    @OneToOne
    private Candidate candidateDetails;

    public MgmtUser(@NotBlank String username, @NotBlank String password, @NotBlank String role, @NotNull Candidate candidateDetails) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.candidateDetails = candidateDetails;
    }

    public Candidate getCandidateDetails() {
        return candidateDetails;
    }

    public void setCandidateDetails(Candidate candidateDetails) {
        this.candidateDetails = candidateDetails;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
