package de.astahsrm.gremiomat.mail;

import java.util.Locale;

import javax.mail.MessagingException;

import de.astahsrm.gremiomat.mgmt.MgmtUser;

public interface MailService {

        public void sendResetPasswordMail(Locale locale, MgmtUser user) throws MessagingException;

        public void sendWelcomeMail(Locale locale, MgmtUser user) throws MessagingException;
}
