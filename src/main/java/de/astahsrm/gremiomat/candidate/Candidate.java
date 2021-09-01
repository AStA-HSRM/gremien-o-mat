package de.astahsrm.gremiomat.candidate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
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
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Gremium> gremium;

    @NotNull
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Answer> answers;

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
        this.gremium = new ArrayList<>();
        this.answers = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Candidate [email=" + email + ", firstname=" + firstname + ", id=" + id + ", lastname=" + lastname + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((imageFileName == null) ? 0 : imageFileName.hashCode());
        result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
        result = prime * result + ((mimeType == null) ? 0 : mimeType.hashCode());
        result = prime * result + (int) (version ^ (version >>> 32));
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
        if (firstname == null) {
            if (other.firstname != null)
                return false;
        } else if (!firstname.equals(other.firstname))
            return false;
        if (id != other.id)
            return false;
        if (imageFileName == null) {
            if (other.imageFileName != null)
                return false;
        } else if (!imageFileName.equals(other.imageFileName))
            return false;
        if (lastname == null) {
            if (other.lastname != null)
                return false;
        } else if (!lastname.equals(other.lastname))
            return false;
        if (mimeType == null) {
            if (other.mimeType != null)
                return false;
        } else if (!mimeType.equals(other.mimeType))
            return false;
        if (version != other.version)
            return false;
        return true;
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

    public List<Gremium> getGremium() {
        return gremium;
    }

    public void setGremium(List<Gremium> gremium) {
        this.gremium = gremium;
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

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public Byte[] getBytes() {
        return bytes;
    }

    public void setBytes(Byte[] bytes) {
        this.bytes = bytes;
    }

}
