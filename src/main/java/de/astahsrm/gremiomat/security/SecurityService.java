package de.astahsrm.gremiomat.security;

import org.springframework.stereotype.Service;

@Service
public interface SecurityService {
    public MgmtUser validatePasswordResetToken(String token);

    public String generateResetToken();

    public String generatePassword();
}