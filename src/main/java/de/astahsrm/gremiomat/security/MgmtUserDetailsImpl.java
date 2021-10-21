package de.astahsrm.gremiomat.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class MgmtUserDetailsImpl implements UserDetailsService {
    @Autowired
    private MgmtUserRepository mgmtUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        MgmtUser user;
        Optional<MgmtUser> userOptional = mgmtUserRepository.findById(username);
        
        if (!userOptional.isPresent() || userOptional.get().isLocked()) {
             return null;
        }
        user = userOptional.get();
        return User.withUsername(username).password(user.getPassword()).roles(user.getRole()).build();
    }
}
