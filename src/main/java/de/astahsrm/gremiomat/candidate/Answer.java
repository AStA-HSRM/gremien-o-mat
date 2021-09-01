package de.astahsrm.gremiomat.candidate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;

import de.astahsrm.gremiomat.gremium.Query;

@Entity
public class Answer {

    @Id
    @GeneratedValue
    private long id;

    @Version
    private long version;

    @NotBlank
    @OneToOne(optional = false, cascade=CascadeType.PERSIST, orphanRemoval=false)
    private Query question;

    @NotBlank
    private int choice;

    public Answer() {
        this.question = new Query();
        this.choice = 0;
    }
}
