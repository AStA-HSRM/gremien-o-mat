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
import de.astahsrm.gremiomat.password.PasswordResetToken;
import de.astahsrm.gremiomat.password.PasswordTokenRepository;

@Service
public class MgmtUserServiceImpl implements MgmtUserService {

    @Autowired
    private MgmtUserRepository mgmtUserRepository;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    @Autowired
    private SecurityService securityService;

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

    @Override
    public Optional<MgmtUser> findUserByEmail(String userEmail) {
        for (MgmtUser user : mgmtUserRepository.findAll()) {
            if (user.hasDetails() && user.getDetails().getEmail().equals(userEmail)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public String createPasswordResetTokenForUser(MgmtUser user) {
        String token = securityService.generateResetToken();
        if (getUserById(user.getUsername()).isPresent()) {
            PasswordResetToken myToken = new PasswordResetToken(token, user);
            passwordTokenRepository.save(myToken);
        }
        return token;
    }
}
