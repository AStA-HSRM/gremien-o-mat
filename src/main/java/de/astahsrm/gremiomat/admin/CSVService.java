package de.astahsrm.gremiomat.admin;

import java.util.List;
import de.astahsrm.gremiomat.candidate.Candidate;

public interface CSVService {
    public List<Candidate> generateCandidatesFromCSV(File csvFile);

}
