package de.astahsrm.gremiomat.username;

import org.springframework.beans.factory.annotation.Autowired;

import de.astahsrm.gremiomat.candidate.Candidate;
import de.astahsrm.gremiomat.security.MgmtUserService;

public class UsernameServiceImpl implements UsernameService {

    @Autowired
    private MgmtUserService mgmtUserService;

    @Override
    public String generateUsername(Candidate c) {
        String username = c.getFirstname().substring(0, 2).concat(c.getLastname().substring(0, 3));
        int count = 0;
        String result = String.format("%s%03d", username, count);
        while (mgmtUserService.getUserById(result).isPresent()) {
            count++;
            result = String.format("%s%03d", username, count);
        }
        return result;
    }
}
