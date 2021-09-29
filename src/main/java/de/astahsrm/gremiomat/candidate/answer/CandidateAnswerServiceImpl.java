package de.astahsrm.gremiomat.candidate.answer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.astahsrm.gremiomat.candidate.Candidate;
import de.astahsrm.gremiomat.candidate.CandidateService;

@Service
public class CandidateAnswerServiceImpl implements CandidateAnswerService {
    @Autowired
    private CandidateAnswerRepository candidateAnswerRepository;

    @Autowired
    private CandidateService candidateService;

    @Override
    public CandidateAnswer saveAnswer(CandidateAnswer ca) {
        return candidateAnswerRepository.save(ca);
    }

    @Override
    public List<CandidateAnswer> getAllCandidateAnswers() {
        return candidateAnswerRepository.findAll();
    }

    @Override
    public void deleteCandidateAnswer(CandidateAnswer ca) {
        for (Candidate c : candidateService.getAllCandidatesSortedByName()) {
            if (c.getAnswers().contains(ca)) {
                c.delAnswer(ca);
                candidateService.saveCandidate(c);
            }
        }
        candidateAnswerRepository.delete(ca);
    }

}
