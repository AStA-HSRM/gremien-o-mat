package de.astahsrm.gremiomat.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import de.astahsrm.gremiomat.mgmt.MgmtUser;
import de.astahsrm.gremiomat.mgmt.MgmtUserRepository;

@Service
public class MgmtUserDetailsImpl implements UserDetailsService {
    @Autowired
    private MgmtUserRepository mgmtUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        MgmtUser user;
        Optional<MgmtUser> userOptional = mgmtUserRepository.findById(username);

        if (!userOptional.isPresent()) {
            for (MgmtUser userToCheck : mgmtUserRepository.findAll()) {
                if (userToCheck.getEmail().toLowerCase().equals(username.toLowerCase())) {
                    if (userToCheck.isLocked()) {
                        return null;
                    } else {
                        return User.withUsername(userToCheck.getUsername()).password(userToCheck.getPassword()).roles(userToCheck.getRole()).build();
                    }
                }
            }
            return null;
        } else {
            user = userOptional.get();
            return User.withUsername(username).password(user.getPassword()).roles(user.getRole()).build();
        }
    }
}
