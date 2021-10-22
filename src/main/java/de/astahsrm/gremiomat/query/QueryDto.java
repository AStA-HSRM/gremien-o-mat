package de.astahsrm.gremiomat.query;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class QueryDto {

    @Min(-1)
    @Max(2)
    private int opinion;

    public QueryDto() {
        this.opinion = 2;
    }

    public int getOpinion() {
        return opinion;
    }
    public void setOpinion(int opinion) {
        this.opinion = opinion;
    }
}
