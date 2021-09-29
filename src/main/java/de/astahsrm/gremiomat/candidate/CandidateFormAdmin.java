package de.astahsrm.gremiomat.candidate;

import java.util.List;

import de.astahsrm.gremiomat.gremium.Gremium;

public class CandidateFormAdmin {

    private int age;
    private int semester;
    private long id;
    private String firstname;
    private String lastname;
    private String email;
    private String course;
    private String bio;
    private List<Gremium> gremien;

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

    public List<Gremium> getGremien() {
        return gremien;
    }

    public void setGremien(List<Gremium> gremien) {
        this.gremien = gremien;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
