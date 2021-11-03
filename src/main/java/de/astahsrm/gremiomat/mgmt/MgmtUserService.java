package de.astahsrm.gremiomat.mgmt;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.naming.AuthenticationException;

import com.opencsv.exceptions.CsvException;

import org.springframework.context.NoSuchMessageException;
import org.springframework.web.multipart.MultipartFile;

import de.astahsrm.gremiomat.candidate.Candidate;

public interface MgmtUserService {
    String USER_NOT_FOUND = "No such User exists.";

    public String getRoleOfUserById(String uid);

    public Candidate getCandidateDetailsOfUser(String uid);

    public Optional<MgmtUser> getUserById(String name);

    public MgmtUser saveUser(MgmtUser u);

    public void saveNewUser(Candidate c, Locale locale) throws NoSuchMessageException, MessagingException;

    public List<MgmtUser> getAllUsersSortedByUsername();

    public void delUser(MgmtUser username);

    public void delUserById(String username);

    public void lockAllUsers();

    public void unlockAllUsers();

    public boolean areUsersLocked();

    public Optional<MgmtUser> findUserByEmail(String userEmail);

    public String createPasswordResetTokenForUser(MgmtUser user);

    public void changePassword(String token, String newPassword) throws AuthenticationException;

    public void saveNewAdmin(Locale locale, String firstname, String lastname, String email) throws NoSuchMessageException, MessagingException;

    public void saveUsersFromCSV(MultipartFile csvFile, String gremiumAbbr, Locale locale)
            throws IOException, CsvException, NoSuchMessageException, MessagingException;

}
