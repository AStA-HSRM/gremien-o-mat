package de.astahsrm.gremiomat.security;

import org.springframework.stereotype.Service;

import de.astahsrm.gremiomat.password.PasswordResetToken;

@Service
public interface SecurityService {
    public PasswordResetToken validatePasswordResetToken(String token);

    public String generateResetToken();

    public String generatePassword();
}