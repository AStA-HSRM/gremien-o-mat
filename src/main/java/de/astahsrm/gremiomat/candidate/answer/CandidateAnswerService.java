package de.astahsrm.gremiomat.candidate.answer;

import java.util.List;

public interface CandidateAnswerService {
    public CandidateAnswer saveAnswer(CandidateAnswer ca);

    public List<CandidateAnswer> getAllCandidateAnswers();

    public void deleteCandidateAnswer(CandidateAnswer ca);
}
