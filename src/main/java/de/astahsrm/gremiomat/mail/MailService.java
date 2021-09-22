package de.astahsrm.gremiomat.mail;

import de.astahsrm.gremiomat.candidate.Candidate;

public interface MailService {
    public void sendWelcomeMailToCandidate(Candidate candidate, String plainPassword);  
}
