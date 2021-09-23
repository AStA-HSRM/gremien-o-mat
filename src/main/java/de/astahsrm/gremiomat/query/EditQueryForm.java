package de.astahsrm.gremiomat.query;

import java.util.ArrayList;
import java.util.List;

import de.astahsrm.gremiomat.gremium.Gremium;

public class EditQueryForm {
    private String queryTxt;
    private String reason;
    private int opinion;
    private List<Gremium> gremien;
    private long id;

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
        if(this.gremien == null) {
            this.gremien = new ArrayList<>();
        }
        for (int i = 0; i < gremien.size(); i++) {
            if (gremien.get(i) instanceof Gremium) {
                this.gremien.add(gremien.get(i));
            }
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
