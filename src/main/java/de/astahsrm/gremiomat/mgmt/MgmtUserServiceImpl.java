package de.astahsrm.gremiomat.mgmt;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.astahsrm.gremiomat.candidate.Candidate;
import de.astahsrm.gremiomat.candidate.CandidateService;
import de.astahsrm.gremiomat.mail.MailService;
import de.astahsrm.gremiomat.password.PasswordToken;
import de.astahsrm.gremiomat.password.PasswordTokenService;
import de.astahsrm.gremiomat.security.SecurityConfig;

@Service
public class MgmtUserServiceImpl implements MgmtUserService {

    @Autowired
    private MgmtUserRepository mgmtUserRepository;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private PasswordTokenService passwordTokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

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
        return mgmtUserRepository.findAll(Sort.by(Direction.DESC, "username")).stream().collect(Collectors.toList());
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
        String tokenStr = passwordTokenService.generateResetToken();
        if (getUserById(user.getUsername()).isPresent()) {
            PasswordToken token = new PasswordToken(tokenStr, user);
            passwordTokenService.save(token);
        }
        return tokenStr;
    }

    @Override
    public void changePassword(String token, String newPassword) throws AuthenticationException {
        PasswordToken pToken = passwordTokenService.validatePasswordResetToken(token);
        if (pToken != null) {
            Optional<MgmtUser> mOpt = mgmtUserRepository.findById(pToken.getUser().getUsername());
            if (mOpt.isPresent()) {
                MgmtUser user = mOpt.get();
                user.setPassword(passwordEncoder.encode(newPassword));
                saveUser(user);
                passwordTokenService.deleteToken(pToken);
                return;
            } else {
                throw new EntityNotFoundException();
            }
        }
        throw new AuthenticationException("Token is invalid.");
    }

    @Override
    public void saveNewUser(Candidate c, Locale locale) {
        mailService.sendWelcomeMail(locale, saveUser(new MgmtUser(generateUsername(c), generatePassword(), c)));
    }

    private String generateUsername(Candidate c) {
        String username = c.getFirstname().substring(0, 2).concat(c.getLastname().substring(0, 3));
        int count = 0;
        String result = String.format("%s%03d", username, count);
        while (getUserById(result).isPresent()) {
            count++;
            result = String.format("%s%03d", username, count);
        }
        return result;
    }

    private String generatePassword() {
        String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(2);
        String specialChar = RandomStringUtils.random(2, 33, 47, false, false);
        String totalChars = RandomStringUtils.randomAlphanumeric(2);
        String combinedChars = upperCaseLetters.concat(lowerCaseLetters).concat(numbers).concat(specialChar)
                .concat(totalChars);
        List<Character> pwdChars = combinedChars.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        return pwdChars.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
    }

    @Override
    public void unlockAllUsers() {
        for (MgmtUser user : mgmtUserRepository.findAll()) {
            user.setLocked(false);
            saveUser(user);
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
    public boolean areUsersLocked() {
        for (MgmtUser user : mgmtUserRepository.findAll()) {
            if(user.getRole().equals(SecurityConfig.USER)) {
                return user.isLocked();
            }
        }
        return true;
    }
}
