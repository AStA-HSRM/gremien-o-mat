package de.astahsrm.gremiomat.faculty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;

@Entity
public class Faculty {

    @Version
    private long version;

    @NotBlank
    private String name;

    @Id
    @NotBlank
    private String abbr;

    public Faculty() {
        this.name = "";
        this.abbr = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

}
