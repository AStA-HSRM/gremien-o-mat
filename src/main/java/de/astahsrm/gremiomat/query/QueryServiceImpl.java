package de.astahsrm.gremiomat.query;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.astahsrm.gremiomat.candidate.answer.CandidateAnswer;
import de.astahsrm.gremiomat.candidate.answer.CandidateAnswerService;
import de.astahsrm.gremiomat.gremium.Gremium;
import de.astahsrm.gremiomat.gremium.GremiumService;

@Service
public class QueryServiceImpl implements QueryService {

    @Autowired
    private QueryRepository queryRepository;

    @Autowired
    private GremiumService gremiumService;

    @Autowired
    private CandidateAnswerService candidateAnswerService;

    @Override
    public Query saveQuery(Query query) {
        return queryRepository.save(query);
    }

    @Override
    public Optional<Query> getQueryById(long queryId) {
        return queryRepository.findById(queryId);
    }

    @Override
    public Optional<Query> getQueryByTxt(String queryTxt) {
        Query flag = new Query();
        flag.setText(queryTxt);
        if (queryRepository.findAll().contains(flag)) {
            return Optional.of(queryRepository.findAll().get(queryRepository.findAll().indexOf(flag)));
        }
        return Optional.empty();
    }

    @Override
    public void delQueryById(long queryId) {
        Optional<Query> qOpt = getQueryById(queryId);
        if (qOpt.isPresent()) {
            Query q = qOpt.get();
            delQuery(q);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public void delQuery(Query q) {
        for (Gremium g : q.getGremien()) {
            g.delQuery(q);
            gremiumService.saveGremium(g);
        }
        for (CandidateAnswer ca : candidateAnswerService.getAllCandidateAnswers()) {
            if (ca.getQuery().equals(q)) {
                candidateAnswerService.deleteCandidateAnswer(ca);
            }
        }
        queryRepository.delete(q);
    }
}
