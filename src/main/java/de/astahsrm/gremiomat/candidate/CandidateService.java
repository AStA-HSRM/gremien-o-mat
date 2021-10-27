package de.astahsrm.gremiomat.candidate;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.opencsv.exceptions.CsvException;

import org.springframework.web.multipart.MultipartFile;

import de.astahsrm.gremiomat.candidate.answer.CandidateAnswer;

public interface CandidateService {
    String CANDIDATE_NOT_FOUND = "No such Candidate exists.";

    public Candidate saveCandidate(Candidate candidate);

    public Optional<Candidate> getCandidateById(long id);

    public void delCandidateById(long id);

    public void delCandidate(Candidate c);

    public List<Candidate> getAllCandidatesSortedByName();

    Optional<CandidateAnswer> getCandidateAnswerByQueryTxt(String queryTxt, long id);

    public boolean candidateExists(Candidate c);

    public void saveCandidatesFromCSV(MultipartFile csvFile, String gremiumAbbr, Locale locale) throws IOException, CsvException;

}
