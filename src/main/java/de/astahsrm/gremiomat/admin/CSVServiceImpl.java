package de.astahsrm.gremiomat.admin;

import java.io.Reader;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.InputStreamReader;

import de.astahsrm.gremiomat.candidate.Candidate;
import de.astahsrm.gremiomat.candidate.CandidateService;
import de.astahsrm.gremiomat.gremium.Gremium;

@Service
public class CSVServiceImpl implements CSVService {

        @Autowired
        private CandidateService candidateService;
   
        /*
            This function assumes that the first name of a candidate is placed in the first column of a CSV file,
            the last name in the second column and the mail address in the third column, like so:
            
            --------------------------------------------------------
            | First Name | Last Name  | Mail Address               |
            --------------------------------------------------------
            --------------------------------------------------------
            | Max        | Mustermann | max.mustermann@example.com |
            --------------------------------------------------------
            | John       | Doe        | john.doe@example.com       |
            --------------------------------------------------------
        */


        @Override
        public List<Candidate> generateCandidatesFromCSV(MultipartFile csvFile, Gremium gremium) throws IOException, CsvException {

            // List<Candidate> candidateList = new ArrayList<>();

            try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(csvFile.getInputStream())).withSkipLines(1).build()) {

                List<String[]> stringArrayList = reader.readAll();
                
                for (String[] entry : stringArrayList) {

                    Candidate candidate = new Candidate();
                    List<Gremium> gremiumList = new ArrayList<Gremium>();

                    Optional<Candidate> candidateOptional = candidateService.getCandidateById(entry[2]);

                    if (candidateOptional.isPresent()) {

                        candidate = candidateOptional.get();
                        gremiumList = candidate.getGremien();
                        gremiumList.add(gremium);
                        candidate.setGremien(gremiumList);
                        candidateService.saveCandidate(candidate);

                    } 
                        else 
                    {

                        gremiumList.add(gremium);

                        candidate.setFirstname(entry[0]);
                        candidate.setLastname(entry[1]);
                        candidate.setEmail(entry[2]);
                        candidate.setGremien(gremiumList);
                        
                        candidateService.saveCandidate(candidate);

                    }
                }
            }

            return null;
        }
}
