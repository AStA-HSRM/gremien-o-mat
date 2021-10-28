package de.astahsrm.gremiomat.gremium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

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
    public Optional<Gremium> findGremiumByAbbr(String abbr) {
        return gremiumRepository.findById(abbr);
    }

    @Override
    public void delByAbbrGremium(String abbr) {
        Optional<Gremium> gremiumOptional = findGremiumByAbbr(abbr);
        if (gremiumOptional.isPresent()) {
            Gremium gremium = gremiumOptional.get();
            for (Candidate cand : gremium.getCandidates()) {
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
        Optional<Gremium> gremiumOptional = findGremiumByAbbr(abbr);
        if (gremiumOptional.isPresent()) {
            return Arrays.asList(gremiumOptional.get().getCandidates().toArray(new Candidate[0]));
        } else {
            throw new NotFoundException(GREMIUM_NOT_FOUND);
        }
    }

    @Override
    public List<Query> getGremiumQueriesByGremiumAbbr(String abbr) throws NotFoundException {
        Optional<Gremium> gremiumOptional = findGremiumByAbbr(abbr);
        if (gremiumOptional.isPresent()) {
            return Arrays.asList(gremiumOptional.get().getQueries().toArray(new Query[0]));
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public void addCandidateToGremium(Candidate candidate, Gremium gremium) {
        if (!gremium.getCandidates().contains(candidate)) {
            gremium.addCandidate(candidate);
            gremiumRepository.save(gremium);
        } else {
            gremium.delCandidate(candidate);
            gremium.addCandidate(candidate);
            gremiumRepository.save(gremium);
        }
    }

    @Override
    public void delCandidateFromGremien(Candidate candidate) {
        for (Gremium g : candidate.getGremien()) {
            Gremium gremium = gremiumRepository.getById(g.getAbbr());
            gremium.delCandidate(candidate);
            saveGremium(gremium);
        }
    }

    @Override
    public void addQueryToGremium(Query query, Gremium gremium) {
        gremium.addQuery(query);
        gremiumRepository.save(gremium);
    }

    @Override
    public HashMap<String, Object> getGremienNavMap() {
        HashMap<String, Object> m = new HashMap<>();
        List<Gremium> fsrGremien = new ArrayList<>();
        List<Gremium> fbrGremien = new ArrayList<>();
        List<Gremium> rest = new ArrayList<>();
        for (Gremium gremium : getAllGremiumsSortedByName()) {
            if (gremium.getAbbr().contains("fbr")) {
                fbrGremien.add(gremium);
            } else if (gremium.getAbbr().contains("fsr")) {
                fsrGremien.add(gremium);
            } else {
                rest.add(gremium);
            }
        }
        m.put("rest", rest);
        m.put("fsr", fsrGremien);
        m.put("fbr", fbrGremien);
        return m;
    }

}
