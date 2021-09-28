package de.astahsrm.gremiomat.gremium;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import de.astahsrm.gremiomat.candidate.Candidate;
import de.astahsrm.gremiomat.candidate.CandidateService;
import de.astahsrm.gremiomat.query.Query;
import javassist.NotFoundException;

@Service
public class GremiumServiceImpl implements GremiumService {
    @Autowired
    private GremiumRepository gremiumRepository;

    @Autowired
    private CandidateService candidateService;

    @Override
    public Gremium saveGremium(Gremium gremium) {
        return gremiumRepository.save(gremium);
    }

    @Override
    public Optional<Gremium> getGremiumByAbbr(String abbr) {
        return gremiumRepository.findById(abbr);
    }

    @Override
    public void delByAbbrGremium(String abbr) {
        Optional<Gremium> gremiumOptional = getGremiumByAbbr(abbr);
        if (gremiumOptional.isPresent()) {
            Gremium gremium = gremiumOptional.get();
            for (Candidate cand : gremium.getJoinedCandidates()) {
                Optional<Candidate> cOptional = candidateService.getCandidateById(cand.getId());
                if (cOptional.isPresent()) {
                    Candidate candidate = cOptional.get();
                    candidate.delGremium(gremium);
                    candidateService.saveCandidate(candidate);
                }
            }
        }
        gremiumRepository.deleteById(abbr);
    }

    @Override
    public List<Gremium> getAllGremiumsSortedByName() {
        return gremiumRepository.findAll(Sort.by(Direction.DESC, "name"));
    }

    @Override
    public List<Candidate> getGremiumCandidatesByGremiumAbbr(String abbr) throws NotFoundException {
        Optional<Gremium> gremiumOptional = getGremiumByAbbr(abbr);
        if (gremiumOptional.isPresent()) {
            return gremiumOptional.get().getJoinedCandidates();
        } else {
            throw new NotFoundException(GREMIUM_NOT_FOUND);
        }
    }

    @Override
    public List<Query> getGremiumQueriesByGremiumAbbr(String abbr) throws NotFoundException {
        Optional<Gremium> gremiumOptional = getGremiumByAbbr(abbr);
        if (gremiumOptional.isPresent()) {
            return gremiumOptional.get().getContainedQueries();
        } else {
            throw new NotFoundException(GREMIUM_NOT_FOUND);
        }
    }

    @Override
    public void addCandidateToGremium(Candidate candidate, Gremium gremium) {
        gremium.addCandidate(candidate);
        gremiumRepository.save(gremium);
    }

    @Override
    public void addQueryToGremium(Query query, Gremium gremium) {
        gremium.addQuery(query);
        gremiumRepository.save(gremium);
    }
}
