package de.astahsrm.gremiomat.candidate;

import java.util.List;
import java.util.Optional;

import de.astahsrm.gremiomat.candidate.answer.CandidateAnswer;

public interface CandidateService {
    String CANDIDATE_NOT_FOUND = "No such Candidate exists.";

    public Candidate saveCandidate(Candidate candidate);

    public Optional<Candidate> getCandidateById(long id);

    public void delCandidateById(long id);

    public void delCandidate(Candidate c);

    public List<Candidate> getAllCandidatesSortedByName();

    Optional<CandidateAnswer> getCandidateAnswerByQueryTxt(String queryTxt, long id);

}
