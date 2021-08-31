package de.astahsrm.gremiomat.candidate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import org.springframework.beans.factory.annotation.Autowired;

public class CandidateServiceImp implements CandidateService {

    @Autowired 
    CandidateRepository cRepo;

    private static final Logger LOGGER = LoggerFactory.getLogger(CandidateServiceImp.class);

    @Override
    public Candidate saveCandidate(Candidate candidate) {
        return cRepo.save(candidate);
    }

    @Override
    public Optional<Candidate> getCandidateById(Long id) {
        return cRepo.findById(id);
    }

    @Override
    public List<Candidate> getAllCandidatesSortedByName() {
        return cRepo.findAll(Sort.by(Direction.DESC, "name"));
    }

    @Override
    public void delCandidate(Long id) {
        cRepo.deleteById(id);
        LOGGER.info("Kandidat gelöscht!");
        // broker.convertAndSend("/topic/foto", new FotoMessage(FotoMessage.FOTO_GELOESCHT, id));
    }
    
}
