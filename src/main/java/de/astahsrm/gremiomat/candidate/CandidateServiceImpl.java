package de.astahsrm.gremiomat.candidate;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import de.astahsrm.gremiomat.gremium.Gremium;
import de.astahsrm.gremiomat.gremium.GremiumService;

@Service
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private GremiumService gremiumService;

    @Override
    public Candidate saveCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    @Override
    public Optional<Candidate> getCandidateById(long id) {
        return candidateRepository.findById(id);
    }

    @Override
    public void delCandidate(long id) {
        Optional<Candidate> cOptional = getCandidateById(id);
        if (cOptional.isPresent()) {
            Candidate c = cOptional.get();
            for (Gremium g : c.getGremien()) {
                Optional<Gremium> gOptional = gremiumService.getGremiumByAbbr(g.getAbbr());
                if (gOptional.isPresent()) {
                    Gremium gremium = gOptional.get();
                    gremium.delCandidate(c);
                    gremiumService.saveGremium(gremium);
                }
            }
        }
        candidateRepository.deleteById(id);
    }

    @Override
    public List<Candidate> getAllCandidatesSortedByName() {
        return candidateRepository
                .findAll(Sort.by(Direction.DESC, "lastname").and(Sort.by(Direction.DESC, "firstname")));
    }

    @Override
    public Optional<CandidateAnswer> getCandidateAnswerByQueryTxt(String queryTxt, long id) {
        Optional<Candidate> candidateOptional = getCandidateById(id);
        if (candidateOptional.isPresent()) {
            Candidate candidate = candidateOptional.get();
            for (CandidateAnswer ele : candidate.getAnswers()) {
                if (ele.getQuery().getText().equals(queryTxt)) {
                    return Optional.of(ele);
                }
            }
            return Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Candidate> getCandidateByEmail(String email) {
        for (Candidate c : candidateRepository.findAll()) {
            if (c.getEmail().equals(email)) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }
}
