package de.astahsrm.gremiomat.query;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import de.astahsrm.gremiomat.gremium.Gremium;

public class QueryAdminDto {

    @NotBlank
    private String txt;

    @NotNull
    private List<String> gremien;

    public QueryAdminDto() {
        this.txt = "";
        this.gremien = new ArrayList<>();
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public List<String> getGremien() {
        return gremien;
    }

    public void setGremien(List<String> gremien) {
        this.gremien = gremien;
    }

    public void addGremium(Gremium g) {
        this.gremien.add(g.getAbbr());
    }

    public void addGremiumAbbr(String abbr) {
        this.gremien.add(abbr);
    }
}
