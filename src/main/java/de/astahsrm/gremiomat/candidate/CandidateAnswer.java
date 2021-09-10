package de.astahsrm.gremiomat.candidate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;

import de.astahsrm.gremiomat.query.Query;

@Entity
public class CandidateAnswer {

    @Id
    @GeneratedValue
    private long id;

    @Version
    private long version;

    @NotBlank
    @OneToOne
    private Query question;

    @NotBlank
    private int choice;

    @Lob
    @NotBlank
    private String reason;

    public CandidateAnswer() {
        this.question = null;
        this.choice = 0;
        this.reason = "";
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
        CandidateAnswer other = (CandidateAnswer) obj;
        return id != other.id;
    }

    public Query getQuestion() {
        return question;
    }

    public void setQuestion(Query question) {
        this.question = question;
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

    public int getChoice() {
        return choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
