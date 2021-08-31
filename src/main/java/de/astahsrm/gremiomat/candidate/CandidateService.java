package de.astahsrm.gremiomat.candidate;

import java.util.List;
import java.util.Optional;

public interface CandidateService {
    public Candidate saveCandidate(Candidate candidate);
    public Optional<Candidate> getCandidateById(Long id);
    public List<Candidate> getAllCandidatesSortedByName();
    public void delCandidate(Long id);
}
