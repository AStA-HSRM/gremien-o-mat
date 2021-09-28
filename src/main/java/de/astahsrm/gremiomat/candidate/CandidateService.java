package de.astahsrm.gremiomat.candidate;

import java.util.List;
import java.util.Optional;

public interface CandidateService {
    String CANDIDATE_NOT_FOUND = "No such Candidate exists.";
    public Candidate saveCandidate(Candidate candidate);
    public Optional<Candidate> getCandidateById(long id);
    public void delCandidate(long id);

    public List<Candidate> getAllCandidatesSortedByName();
    Optional<CandidateAnswer> getCandidateAnswerByQueryTxt(String queryTxt, long id);
    public Optional<Candidate> getCandidateByEmail(String email);
}
