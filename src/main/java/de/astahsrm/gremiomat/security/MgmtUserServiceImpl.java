package de.astahsrm.gremiomat.security;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import de.astahsrm.gremiomat.candidate.Candidate;
import de.astahsrm.gremiomat.candidate.CandidateService;

@Service
public class MgmtUserServiceImpl implements MgmtUserService {

    @Autowired
    private MgmtUserRepository mgmtUserRepository;

    @Autowired
    private CandidateService candidateService;

    @Override
    public String getRoleOfUserById(String uid) {
        return mgmtUserRepository.getById(uid).getRole();
    }

    @Override
    public Candidate getCandidateDetailsOfUser(String uid) {
        return mgmtUserRepository.getById(uid).getDetails();
    }

    @Override
    public Optional<MgmtUser> getUserById(String uid) {
        return mgmtUserRepository.findById(uid);
    }

    @Override
    public MgmtUser saveUser(MgmtUser u) {
        return mgmtUserRepository.save(u);
    }

    @Override
    public List<MgmtUser> getAllUsersSortedByUsername() {
        return mgmtUserRepository.findAll(Sort.by(Direction.DESC, "username")).stream()
                .filter(u -> !u.getUsername().equals("admin")).collect(Collectors.toList());
    }

    @Override
    public void delUser(MgmtUser user) {
        mgmtUserRepository.delete(user);
    }

    @Override
    public void delUserById(String username) {
        Optional<MgmtUser> uOpt = getUserById(username);
        if (uOpt.isPresent()) {
            MgmtUser mu = uOpt.get();
            Candidate c = uOpt.get().getDetails();
            mu.setDetails(null);
            saveUser(mu);
            candidateService.delCandidate(c);
            mgmtUserRepository.delete(mu);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public void lockAllUsers() {
        for (MgmtUser user : mgmtUserRepository.findAll()) {
            user.setLocked(true);
            saveUser(user);
        }
    }
}
