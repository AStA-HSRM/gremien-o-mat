package de.astahsrm.gremiomat.candidate;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;

@Service
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Override
    public Candidate saveCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    @Override
    public Optional<Candidate> getCandidateById(String candidateEmail) {
        return candidateRepository.findById(candidateEmail);
    }

    @Override
    public void delCandidate(String candidateEmail) {
        candidateRepository.deleteById(candidateEmail);
    }

    @Override
    public List<Candidate> getAllCandidatesSortedByName() {
        return candidateRepository.findAll(Sort.by(Direction.DESC, "lastname").and(Sort.by(Direction.DESC, "firstname")));
    }

    @Override
    public Optional<CandidateAnswer> getCandidateAnswerByQueryTxt(String queryTxt, String candidateEmail) throws NotFoundException {
        Optional<Candidate> candidateOptional = getCandidateById(candidateEmail);
        if(candidateOptional.isPresent()) {
            Candidate candidate = candidateOptional.get();
            for (CandidateAnswer ele : candidate.getAnswers()) {
                if(ele.getQuestion().getText().equals(queryTxt)) {
                    return Optional.of(ele);
                }
            }
            return Optional.empty();
        }
        else {
            throw new NotFoundException("No such Candidate exists.");
        }
    }
}
