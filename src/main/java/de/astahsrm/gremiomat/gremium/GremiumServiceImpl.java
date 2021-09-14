package de.astahsrm.gremiomat.gremium;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import de.astahsrm.gremiomat.candidate.Candidate;
import de.astahsrm.gremiomat.query.Query;
import javassist.NotFoundException;

@Service
public class GremiumServiceImpl implements GremiumService {

    @Autowired
    private GremiumRepository gremiumRepository;

    @Override
    public Gremium saveGremium(Gremium gremium) {
        return gremiumRepository.save(gremium);
    }

    @Override
    public Optional<Gremium> getGremiumByAbbr(String abbr) {
        return gremiumRepository.findById(abbr);
    }

    @Override
    public void delGremium(String gremiumAbbr) {
        gremiumRepository.deleteById(gremiumAbbr);
    }

    @Override
    public List<Gremium> getAllGremiumsSortedByName() {
        return gremiumRepository.findAll(Sort.by(Direction.DESC, "name"));
    }

    @Override
    public List<Candidate> getGremiumCandidatesByGremiumAbbr(String gremiumAbbr) throws NotFoundException {
        Optional<Gremium> gremiumOptional = getGremiumByAbbr(gremiumAbbr);
        if (gremiumOptional.isPresent()) {
            return gremiumOptional.get().getCandidates();
        } else {
            throw new NotFoundException("No such Gremium exists.");
        }
    }

    @Override
    public List<Query> getGremiumQueriesByGremiumAbbr(String gremiumAbbr) throws NotFoundException {
        Optional<Gremium> gremiumOptional = getGremiumByAbbr(gremiumAbbr);
        if (gremiumOptional.isPresent()) {
            return gremiumOptional.get().getQueries();
        } else {
            throw new NotFoundException("No such Gremium exists.");
        }
    }
}
