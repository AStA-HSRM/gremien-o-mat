package de.astahsrm.gremiomat.candidate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;

@Entity
public class Answer {

    @Id
    @GeneratedValue
    private long id;

    @Version
    private long version;

    @NotBlank
    @OneToOne(optional = false, cascade=CascadeType.PERSIST, orphanRemoval=false)
    private long questionId;

    @NotBlank
    private int choice;

    public Answer(long questionId) {
        this.questionId = questionId;
        this.choice = 0;
    }
}
