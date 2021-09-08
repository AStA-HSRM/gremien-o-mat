package de.astahsrm.gremiomat.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

@Service
public class MgmtUserDetailsService implements UserDetailsService {
    @Autowired
    private MgmtUserRepository mgmtUserRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        MgmtUser user;
        Optional<MgmtUser> userOptional = mgmtUserRepository.findById(username);
        
        if (!userOptional.isPresent()) {
             return null;
        }
        user = userOptional.get();

        return User.withUsername(username).password(encoder.encode(user.getPassword())).roles(user.getRole()).build();
    }
}
