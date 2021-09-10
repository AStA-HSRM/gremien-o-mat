package de.astahsrm.gremiomat.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MgmtUserServiceImpl implements MgmtUserService {

    @Autowired
    private MgmtUserRepository mgmtUserRepository;

    @Override
    public String getRoleOfUserById(String uid) {
        return mgmtUserRepository.getById(uid).getRole();
    }
}
