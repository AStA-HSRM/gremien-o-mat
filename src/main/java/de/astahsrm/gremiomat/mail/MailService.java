package de.astahsrm.gremiomat.mail;

import javax.mail.MessagingException;

public interface MailService {
    public void sendMail(String to) throws MessagingException;
}
