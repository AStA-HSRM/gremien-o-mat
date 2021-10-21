package de.astahsrm.gremiomat.mail;

import java.util.Locale;

import de.astahsrm.gremiomat.security.MgmtUser;

public interface MailService {
    
    public void sendResetPasswordMail(String appUrl, Locale locale, String token, MgmtUser user);

    public void sendWelcomeMail(Locale locale, MgmtUser user);
}
