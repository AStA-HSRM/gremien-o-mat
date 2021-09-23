package de.astahsrm.gremiomat.query;

import java.util.List;

import de.astahsrm.gremiomat.gremium.Gremium;

public class EditQueryForm {
    private String queryTxt;
    private String reason;
    private int opinion;
    private List<Gremium> gremien;

    public String getQueryTxt() {
        return queryTxt;
    }
    public void setQueryTxt(String queryTxt) {
        this.queryTxt = queryTxt;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public int getOpinion() {
        return opinion;
    }
    public void setOpinion(int opinion) {
        this.opinion = opinion;
    }
    public List<Gremium> getGremien() {
        return gremien;
    }
    public void setGremien(List<Gremium> gremien) {
        this.gremien = gremien;
    }
}
