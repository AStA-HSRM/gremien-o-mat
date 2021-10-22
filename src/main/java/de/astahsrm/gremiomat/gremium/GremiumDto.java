package de.astahsrm.gremiomat.gremium;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class GremiumDto {

    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 3, max = 9)
    private String abbr;
    
    @NotBlank
    @Size(max = 500)
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
