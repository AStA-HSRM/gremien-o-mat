package de.astahsrm.gremiomat.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.astahsrm.gremiomat.candidate.Candidate;

@Service
public class MgmtUserServiceImpl implements MgmtUserService {

    @Autowired
    private MgmtUserRepository mgmtUserRepository;

    @Override
    public String getRoleOfUserById(String uid) {
        return mgmtUserRepository.getById(uid).getRole();
    }

    @Override
    public Candidate getCandidateDetailsOfUser(String uid) {
        return mgmtUserRepository.getById(uid).getCandidateDetails();
    }

    @Override
    public Optional<MgmtUser> getUserById(String uid) {
        return mgmtUserRepository.findById(uid);
    }

    @Override
    public MgmtUser saveUser(MgmtUser u) {
        return mgmtUserRepository.save(u);
    }
}
