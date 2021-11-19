package de.astahsrm.gremiomat.candidate.answer;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import de.astahsrm.gremiomat.query.Query;

public class CandidateAnswerDto {
    @NotNull
    private long answerId;

    @NotNull
    private Query query;
    
    @Min(-1)
    @Max(2)
    private int opinion;
    
    @NotBlank
    private String reason;

    public CandidateAnswerDto() {
        this.opinion = 2;
        this.reason = "";
    }

    public long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(long answerId) {
        this.answerId = answerId;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public int getOpinion() {
        return opinion;
    }

    public void setOpinion(int opinion) {
        this.opinion = opinion;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
