package de.astahsrm.gremiomat.gremium;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import de.astahsrm.gremiomat.candidate.Candidate;
import de.astahsrm.gremiomat.query.Query;

@Service
public class GremiumServiceImpl implements GremiumService {

    @Autowired
    private GremiumRepository gremiumRepository;

    @Override
    public Optional<Gremium> getGremiumById(Long id) {
        return gremiumRepository.findById(id);
    }

    @Override
    public void delGremium(Long id) {
        gremiumRepository.deleteById(id);
    }

    @Override
    public Gremium saveGremium(Gremium gremium) {
        return gremiumRepository.save(gremium);
    }

    @Override
    public List<Gremium> getAllGremiumsSortedByName() {
        return gremiumRepository.findAll(Sort.by(Direction.DESC, "name"));
    }

    @Override
    public Optional<Gremium> getGremiumByAbbr(String abbr) {
        for (Gremium gremium : gremiumRepository.findAll()) {
            if (gremium.getAbbr().equals(abbr)) {
                return Optional.of(gremium);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Candidate> getGremiumCandidatesById(Long id) {
        Optional<Gremium> gremiumOptional = gremiumRepository.findById(id);
        if(gremiumOptional.isPresent()) {
            return gremiumOptional.get().getCandidates();
        }
        else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Query> getGremiumQueriesById(Long id) {
        Optional<Gremium> gremiumOptional = gremiumRepository.findById(id);
        if(gremiumOptional.isPresent()) {
            return gremiumOptional.get().getQueries();
        }
        else {
            return new ArrayList<>();
        }
    }
}
