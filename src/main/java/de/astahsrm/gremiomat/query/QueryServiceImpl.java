package de.astahsrm.gremiomat.query;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
        for (Gremium gremium : gremiumService.getAllGremiumsSortedByName()) {
            if(query.getGremien().contains(gremium)) {
                gremium.addQuery(query);
            }
            else {
                gremium.delQuery(query);
            }
            gremiumService.saveGremium(gremium);
        } 
        return queryRepository.getById(query.getId());
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

    @Override
    public void saveQueriesFromCSV(MultipartFile csvFile, String gremiumAbbr)
            throws IOException, CsvException {
        Optional<Gremium> gremiumOptional = gremiumService.findGremiumByAbbr(gremiumAbbr);
        if (gremiumOptional.isPresent()) {
            Gremium gremium = gremiumOptional.get();
            CSVReader reader = new CSVReaderBuilder(new InputStreamReader(csvFile.getInputStream())).withSkipLines(1)
                    .build();
            String[] entry;
            while ((entry = reader.readNext()) != null) {
                Optional<Query> qOpt = getQueryByTxt(entry[0]);
                Query q = null;
                if (qOpt.isPresent()) {
                    q = qOpt.get();
                } else {
                    q = new Query();
                }
                q.addGremium(gremium);
                q.setText(entry[0]);
                gremiumService.addQueryToGremium(saveQuery(q), gremium);
            }
        } else {
            throw new EntityNotFoundException();
        }
    }
}
