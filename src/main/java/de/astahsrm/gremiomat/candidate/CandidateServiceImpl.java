package de.astahsrm.gremiomat.candidate;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import de.astahsrm.gremiomat.candidate.answer.CandidateAnswer;
import de.astahsrm.gremiomat.candidate.answer.CandidateAnswerService;
import de.astahsrm.gremiomat.gremium.Gremium;
import de.astahsrm.gremiomat.gremium.GremiumService;
import de.astahsrm.gremiomat.query.Query;

@Service
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CandidateAnswerService candidateAnswerService;

    @Autowired
    private GremiumService gremiumService;

    @Override
    public Candidate saveCandidate(Candidate candidate) {
        if (candidate.getAnswers().isEmpty()) {
            HashSet<Query> queries = new HashSet<>();
            for (Gremium gremium : candidate.getGremien()) {
                queries.addAll(gremium.getQueries());
            }
            for (Query query : queries) {
                CandidateAnswer ca = new CandidateAnswer();
                ca.setQuery(query);
                ca.setOpinion(2);
                candidate.addNewAnswer(candidateAnswerService.saveAnswer(ca));
            }
        }
        return candidateRepository.save(candidate);
    }

    @Override
    public Optional<Candidate> getCandidateById(long id) {
        return candidateRepository.findById(id);
    }

    @Override
    public void delCandidateById(long id) {
        Optional<Candidate> cOptional = getCandidateById(id);
        if (cOptional.isPresent()) {
            candidateRepository.delete(cOptional.get());
        } else {
            throw new EntityNotFoundException();
        }
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
    public void delCandidate(Candidate c) {
        for (Gremium g : c.getGremien()) {
            Optional<Gremium> gOptional = gremiumService.findGremiumByAbbr(g.getAbbr());
            if (gOptional.isPresent()) {
                Gremium gremium = gOptional.get();
                gremium.delCandidate(c);
                gremiumService.saveGremium(gremium);
            }
        }
        candidateRepository.delete(c);
    }

}
