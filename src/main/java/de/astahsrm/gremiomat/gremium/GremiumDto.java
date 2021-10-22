package de.astahsrm.gremiomat.gremium;

import javax.validation.constraints.NotBlank;

public class GremiumDto {

    @NotBlank
    private String name;

    @NotBlank
    private String abbr;
    
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
