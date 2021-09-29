package de.astahsrm.gremiomat.candidate.answer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateAnswerServiceImpl implements CandidateAnswerService {
    @Autowired
    private CandidateAnswerRepository candidateAnswerRepository;

    @Override
    public CandidateAnswer saveAnswer(CandidateAnswer ca) {
        return candidateAnswerRepository.save(ca);
    }
}
