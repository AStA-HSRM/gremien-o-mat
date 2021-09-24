package de.astahsrm.gremiomat.csv;

import java.io.IOException;

import com.opencsv.exceptions.CsvException;

import org.springframework.web.multipart.MultipartFile;

import javassist.NotFoundException;

public interface CSVService {
    public void saveCandidatesFromCSV(MultipartFile csvFile, String gremiumAbbr) throws IOException, CsvException, NotFoundException;
    public void saveQueriesFromCSV(MultipartFile csvFile, String gremiumAbbr) throws IOException, CsvException, NotFoundException;
}
