package de.astahsrm.gremiomat.mgmt;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import de.astahsrm.gremiomat.candidate.Candidate;
import de.astahsrm.gremiomat.candidate.CandidateService;
import de.astahsrm.gremiomat.gremium.Gremium;
import de.astahsrm.gremiomat.gremium.GremiumService;
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
    private GremiumService gremiumService;

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
        if (u.hasDetails()) {
            candidateService.saveCandidate(u.getDetails());
        }
        return mgmtUserRepository.save(u);
    }

    @Override
    public List<MgmtUser> getAllUsersSortedByUsername() {
        List<MgmtUser> list = mgmtUserRepository.findAll(Sort.by(Direction.ASC, "username")).stream()
                .collect(Collectors.toList());
        Collections.sort(list, (u1, u2) -> {
            String role1 = u1.getRole();
            String role2 = u2.getRole();
            return role1.compareTo(role2);
        });
        return list;
    }

    @Override
    public void delUser(MgmtUser user) {
        passwordTokenService.deleteTokenByUsername(user.getUsername());
        if (user.hasDetails()) {
            Candidate c = user.getDetails();
            user.setDetails(null);
            saveUser(user);
            candidateService.delCandidate(c);
        }
        mgmtUserRepository.delete(user);
    }

    @Override
    public void delUserById(String username) {
        Optional<MgmtUser> uOpt = getUserById(username);
        if (uOpt.isPresent()) {
            delUser(uOpt.get());
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public Optional<MgmtUser> findUserByEmail(String userEmail) {
        for (MgmtUser user : mgmtUserRepository.findAll()) {
            if (user.getEmail().equals(userEmail)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public String createPasswordResetTokenForUser(MgmtUser user) {
        String tokenStr = passwordTokenService.generateResetToken();
        if (getUserById(user.getUsername()).isPresent()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_YEAR, 14);
            PasswordToken token = new PasswordToken(tokenStr, user, calendar.getTime());
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
    public void saveNewUser(String email, Candidate c, Locale locale)
            throws NoSuchMessageException, MessagingException {
        mailService.sendWelcomeMail(locale, saveUser(
                new MgmtUser(email, generateUsername(c.getFirstname(), c.getLastname()), generatePassword(), c)));
    }

    private String generateUsername(String firstname, String lastname) {
        String username = firstname.substring(0, 2).concat(lastname.substring(0, 3));
        int count = 0;
        String result = String.format("%s%03d", username, count);
        while (getUserById(result).isPresent()) {
            count++;
            result = String.format("%s%03d", username, count);
        }
        return result.toLowerCase();
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
        lockUsers(false);
    }

    @Override
    public void lockAllUsers() {
        lockUsers(true);
    }

    private void lockUsers(boolean lock) {
        for (MgmtUser user : mgmtUserRepository.findAll()) {
            if (user.getRole().equals(SecurityConfig.USER)) {
                user.setLocked(lock);
                saveUser(user);
            }
        }
    }

    @Override
    public boolean areUsersLocked() {
        for (MgmtUser user : mgmtUserRepository.findAll()) {
            if (user.getRole().equals(SecurityConfig.USER) && !user.isLocked()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void saveNewAdmin(Locale locale, String firstname, String lastname, String email)
            throws NoSuchMessageException, MessagingException {
        mailService.sendWelcomeMail(locale,
                saveUser(new MgmtUser(email, generateUsername(firstname, lastname), generatePassword())));
    }

    /*
     * This function assumes that the first name of a candidate is placed in the
     * first column of a CSV file, the last name in the second column and the mail
     * address in the third column, like so:
     * 
     * | Firstname | Lastname | Mail Address |
     */
    @Override
    public void saveUsersFromCSV(MultipartFile csvFile, String abbr, Locale locale)
            throws IOException, CsvException, NoSuchMessageException, MessagingException {
        String[] entry;
        CSVReader reader = new CSVReaderBuilder(new InputStreamReader(csvFile.getInputStream())).withSkipLines(1)
                .build();
        while ((entry = reader.readNext()) != null) {
            Candidate candidate = new Candidate();
            candidate.setFirstname(entry[0]);
            candidate.setLastname(entry[1]);
            candidate = candidateService.saveCandidate(candidate);
            Optional<Gremium> gOpt = gremiumService.findGremiumByAbbr(abbr);
            if (gOpt.isPresent()) {
                candidate.addGremium(gOpt.get());
                gremiumService.addCandidateToGremium(candidate, gOpt.get());
            }
            saveNewUser(entry[2], candidate, locale);
        }
    }

}
