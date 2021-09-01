package de.astahsrm.gremiomat.candidate;

import java.util.List;
import java.util.Optional;

public interface CandidateService {
    public Optional<Candidate> getCandidateById(Long id);

    public List<Candidate> getAllCandidatesSortedByName();

    public List<Candidate> getGremiumCandidatesSortedByName(Long gremiumId);

    public void delCandidate(Long id);

    public Candidate saveCandidate(Candidate candidate);
    
}
