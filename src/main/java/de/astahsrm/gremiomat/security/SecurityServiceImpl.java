package de.astahsrm.gremiomat.security;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;

import de.astahsrm.gremiomat.password.PasswordResetToken;
import de.astahsrm.gremiomat.password.PasswordTokenRepository;

public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    public String validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
        
        if (!isTokenFound(passToken)) {
            if (isTokenExpired(passToken)) {
                return null;
            } else {
                return "expired";
            }
        } else {
            return "invalidToken";
        }
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }

}
