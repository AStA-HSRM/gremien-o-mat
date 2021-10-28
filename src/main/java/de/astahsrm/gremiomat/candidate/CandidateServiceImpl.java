package de.astahsrm.gremiomat.candidate;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import de.astahsrm.gremiomat.candidate.answer.CandidateAnswer;
import de.astahsrm.gremiomat.gremium.Gremium;
import de.astahsrm.gremiomat.gremium.GremiumService;
import de.astahsrm.gremiomat.security.MgmtUserService;

@Service
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private MgmtUserService mgmtUserService;

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

    @Override
    public boolean candidateExists(Candidate c) {
        for (Candidate candidate : candidateRepository.findAll()) {
            if (c.equals(candidate)) {
                return true;
            }
        }
        return false;
    }

    /*
     * This function assumes that the first name of a candidate is placed in the
     * first column of a CSV file, the last name in the second column and the mail
     * address in the third column, like so:
     * 
     * -------------------------------------------------------- | First Name | Last
     * Name | Mail Address |
     * --------------------------------------------------------
     * -------------------------------------------------------- | Max | Mustermann |
     * max.mustermann@example.com |
     * -------------------------------------------------------- | John | Doe |
     * john.doe@example.com |
     * --------------------------------------------------------
     */
    @Override
    public void saveCandidatesFromCSV(MultipartFile csvFile, String gremiumAbbr, Locale locale)
            throws IOException, CsvException {
        Optional<Gremium> gremiumOptional = gremiumService.findGremiumByAbbr(gremiumAbbr);
        if (gremiumOptional.isPresent()) {
            Gremium gremium = gremiumOptional.get();
            CSVReader reader = new CSVReaderBuilder(new InputStreamReader(csvFile.getInputStream())).withSkipLines(1)
                    .build();
            String[] entry;
            while ((entry = reader.readNext()) != null) {
                Candidate candidate = new Candidate();
                candidate.setFirstname(entry[0]);
                candidate.setLastname(entry[1]);
                candidate.setEmail(entry[2]);
                candidate.addGremium(gremium);
                if (candidateExists(candidate)) {
                    gremiumService.delCandidateFromGremien(candidate);
                }
                gremiumService.addCandidateToGremium(saveCandidate(candidate), gremium);
                mgmtUserService.saveNewUser(candidate, locale);
            }
        } else {
            throw new EntityNotFoundException();
        }
    }
}
