package de.astahsrm.gremiomat.candidate.answer;

import javax.validation.constraints.NotNull;

import de.astahsrm.gremiomat.query.Query;

public class CandidateAnswerForm {
    @NotNull
    private long answerId;

    private Query query;
    
    @NotNull
    private int opinion;
    
    private String reason;

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
