package de.astahsrm.gremiomat.candidate;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import de.astahsrm.gremiomat.gremium.Gremium;

public class CandidateDtoAdmin {

    @NotEmpty
    @Min(16)
    @Max(120)
    private int age;

    @NotEmpty
    private int semester;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @Email
    private String email;
    private String course;
    private String bio;
    private Set<Gremium> gremien;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
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

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Gremium> getGremien() {
        return gremien;
    }

    public void setGremien(Set<Gremium> gremien) {
        this.gremien = gremien;
    }

}
