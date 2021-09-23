package de.astahsrm.gremiomat.candidate;

import java.util.List;
import java.util.Optional;

public interface CandidateService {
    public Candidate saveCandidate(Candidate candidate);
    public Optional<Candidate> getCandidateById(String candidateEmail);
    public void delCandidate(String candidateEmail);

    public List<Candidate> getAllCandidatesSortedByName();
    Optional<CandidateAnswer> getCandidateAnswerByQueryTxt(String queryTxt, String candidateEmail);
}
