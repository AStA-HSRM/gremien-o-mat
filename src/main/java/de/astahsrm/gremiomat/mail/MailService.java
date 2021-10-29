package de.astahsrm.gremiomat.mail;

import java.util.Locale;

import de.astahsrm.gremiomat.mgmt.MgmtUser;

public interface MailService {
    
    public void sendResetPasswordMail(Locale locale, MgmtUser user);

    public void sendWelcomeMail(Locale locale, MgmtUser user);
}
