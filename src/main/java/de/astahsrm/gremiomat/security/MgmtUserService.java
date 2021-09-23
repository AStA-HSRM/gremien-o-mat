package de.astahsrm.gremiomat.security;

import de.astahsrm.gremiomat.candidate.Candidate;

public interface MgmtUserService {
    public String getRoleOfUserById(String uid);
    public Candidate getCandidateDetailsOfUser(String uid);
}
