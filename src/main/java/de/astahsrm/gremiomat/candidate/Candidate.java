package de.astahsrm.gremiomat.candidate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.astahsrm.gremiomat.gremium.Gremium;

@Entity
public class Candidate {

    @Id
    @GeneratedValue
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

    @NotEmpty(message = "{notEmpty}")
    private String mimeType;

    @Size(min = 5, message = "{minSize}")
    private String imageFileName;

    @Lob
    @JsonIgnore
    private Byte[] bytes;

    public Candidate() {
        this.mimeType = "";
        this.imageFileName = "";
        this.firstname = "";
        this.lastname = "";
        this.email = "";
        this.answers = new ArrayList<>();
        this.gremien = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Candidate [email=" + email + ", firstname=" + firstname + ", id=" + id + ", lastname=" + lastname + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
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
        if (id != other.id)
            return false;
        return true;
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

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public List<CandidateAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<CandidateAnswer> answers) {
        this.answers = answers;
    }

    public Byte[] getBytes() {
        return bytes;
    }

    public void setBytes(Byte[] bytes) {
        this.bytes = bytes;
    }

}
