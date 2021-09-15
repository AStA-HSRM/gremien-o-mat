package de.astahsrm.gremiomat.csv;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import de.astahsrm.gremiomat.candidate.Candidate;
import de.astahsrm.gremiomat.candidate.CandidateService;
import de.astahsrm.gremiomat.gremium.Gremium;
import de.astahsrm.gremiomat.gremium.GremiumService;
import javassist.NotFoundException;

@Service
public class CSVServiceImpl implements CSVService {

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private GremiumService gremiumService;

    /*
     * This function assumes that the first name of a candidate is placed in the
     * first column of a CSV file, the last name in the second column and the mail
     * address in the third column, like so:
     * 
     * -------------------------------------------------------- 
     * | First Name | Last Name | Mail Address |
     * --------------------------------------------------------
     * -------------------------------------------------------- 
     * | Max | Mustermann | max.mustermann@example.com |
     * -------------------------------------------------------- 
     * | John | Doe | john.doe@example.com |
     * --------------------------------------------------------
     */

    @Override
    public void generateCandidatesFromCSV(MultipartFile csvFile, String gremiumAbbr)
            throws IOException, CsvException, NotFoundException {
        Optional<Gremium> gremiumOptional = gremiumService.getGremiumByAbbr(gremiumAbbr);
        if (gremiumOptional.isPresent()) {
            Gremium gremium = gremiumOptional.get();
            try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(csvFile.getInputStream()))
                    .withSkipLines(1).build()) {
                String[] entry;
                while ((entry = reader.readNext()) != null) {
                    Candidate candidate = new Candidate();
                    Optional<Candidate> candidateOptional = candidateService.getCandidateById(entry[2]);
                    if (candidateOptional.isPresent()) {
                        candidate = candidateOptional.get();
                        candidate.addGremium(gremium);
                        gremiumService.addCandidateToGremium(candidateService.saveCandidate(candidate), gremium);
                    } else {
                        candidate.setFirstname(entry[0]);
                        candidate.setLastname(entry[1]);
                        candidate.setEmail(entry[2]);
                        candidate.addGremium(gremium);
                        gremiumService.addCandidateToGremium(candidateService.saveCandidate(candidate), gremium);
                    }
                }
            }
        }
        else {
            throw new NotFoundException("No such Gremium exists.");
        }
    }
}
