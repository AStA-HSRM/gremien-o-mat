package de.astahsrm.gremiomat.candidate;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Override
    public Optional<Candidate> getCandidateById(Long id) {
        return candidateRepository.findById(id);
    }

    // TODO pls check if this is right (probably not)
    @Override
    public List<Candidate> getAllCandidatesSortedByName() {
        return candidateRepository.findAll(Sort.by(Direction.ASC, "lastname").and(Sort.by(Direction.ASC, "firstname")));
    }

    // TODO not implemented fully yet, need getGremiumCandidates function on GremiumService
    //  also pls check if this is right
    @Override
    public List<Candidate> getGremiumCandidatesSortedByName(Long gremiumId) {
        return candidateRepository.findAll();
        
        /* 
        for (Candidate candidate : candidateRepository.findAll()) {
            for (Gremium gremium: candidate.getGremium())
                if (gremium.getId() == gremiumId) {
                    return ??????;
                }
        }
        */
    }

    @Override
    public void delCandidate(Long id) {
        candidateRepository.deleteById(id);
    }

    @Override
    public Candidate saveCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

}
