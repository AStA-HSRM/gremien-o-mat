package de.astahsrm.gremiomat.mail;

import java.util.Locale;

import javax.mail.MessagingException;

import org.springframework.context.NoSuchMessageException;

import de.astahsrm.gremiomat.mgmt.MgmtUser;

public interface MailService {
    
    public void sendResetPasswordMail(Locale locale, MgmtUser user) throws NoSuchMessageException, MessagingException;

    public void sendWelcomeMail(Locale locale, MgmtUser user) throws NoSuchMessageException, MessagingException;

    public void sendAdminWelcomeMail(Locale locale, MgmtUser user, String firstname, String lastname, String email) throws NoSuchMessageException, MessagingException;
}
