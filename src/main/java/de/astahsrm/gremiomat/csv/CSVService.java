package de.astahsrm.gremiomat.csv;

import java.io.IOException;
import java.util.List;

import com.opencsv.exceptions.CsvException;

import org.springframework.web.multipart.MultipartFile;

import de.astahsrm.gremiomat.candidate.Candidate;

public interface CSVService {
    public List<Candidate> generateCandidatesFromCSV(MultipartFile csvFile, String gremiumAbbr)
            throws IOException, CsvException;
}
