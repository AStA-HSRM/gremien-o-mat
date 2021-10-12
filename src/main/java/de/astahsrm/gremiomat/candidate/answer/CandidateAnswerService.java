package de.astahsrm.gremiomat.candidate.answer;

import java.util.List;
import java.util.Optional;

public interface CandidateAnswerService {
    String ANSWER_NOT_FOUND = "This answer does not exist.";

    public Optional<CandidateAnswer> getAnswerById(long id);

    public CandidateAnswer saveAnswer(CandidateAnswer ca);

    public List<CandidateAnswer> getAllCandidateAnswers();

    public void deleteCandidateAnswer(CandidateAnswer ca);
}
