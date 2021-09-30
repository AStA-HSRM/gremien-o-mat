package de.astahsrm.gremiomat.candidate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import de.astahsrm.gremiomat.candidate.answer.CandidateAnswer;
import de.astahsrm.gremiomat.gremium.Gremium;
import de.astahsrm.gremiomat.photo.Photo;

@Entity
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Version
    private long version;

    @NotEmpty(message = "{notEmpty}")
    private String firstname;

    @NotEmpty(message = "{notEmpty}")
    private String lastname;

    @NotEmpty(message = "{notEmpty}")
    private String email;

    @NotNull
    @ManyToMany(mappedBy = "joinedCandidates", cascade = CascadeType.PERSIST)
    private List<Gremium> gremien;

    @NotNull
    @OneToMany
    private List<CandidateAnswer> answers;

    @OneToOne
    private Photo photo;

    @Column(columnDefinition = "integer default 0")
    private int age;

    @Column(columnDefinition = "integer default 0")
    private int semester;

    private String bio;

    private String course;

    public Candidate() {
        this.firstname = "";
        this.lastname = "";
        this.email = "";
        this.bio = "";
        this.answers = new ArrayList<>();
        this.gremien = new ArrayList<>();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Candidate other = (Candidate) obj;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        return true;
    }

    public List<Gremium> getGremien() {
        return gremien;
    }

    @Override
    public String toString() {
        StringBuilder bld = new StringBuilder();
        bld.append("Candidate [email=" + email + ", firstname=" + firstname + ", gremien= {");
        for (Gremium g : this.gremien) {
            bld.append(g.getAbbr() + ",");
        }
        bld.deleteCharAt(bld.toString().length() - 1);
        bld.append("}, lastname=" + lastname + "]");
        return bld.toString();
    }

    public void addGremium(Gremium gremium) {
        if (!this.gremien.contains(gremium))
            this.gremien.add(gremium);
    }

    public void delGremium(Gremium gremium) {
        this.gremien.remove(gremium);
    }

    public void setGremien(List<Gremium> gremien) {
        this.gremien = gremien;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<CandidateAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<CandidateAnswer> answers) {
        this.answers = answers;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

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

    public void addNewAnswer(CandidateAnswer ca) {
        if (!this.answers.contains(ca)) {
            this.answers.add(ca);
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public void delAnswer(CandidateAnswer ca) {
        this.answers.remove(ca);
    }

}
