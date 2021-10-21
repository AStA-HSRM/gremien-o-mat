package de.astahsrm.gremiomat.security;

import org.springframework.stereotype.Service;

@Service
public interface SecurityService {
    public String validatePasswordResetToken(String token);
}