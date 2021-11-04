package de.astahsrm.gremiomat.password;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Calendar;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordTokenServiceImpl implements PasswordTokenService {

    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    private static final SecureRandom random = new SecureRandom();

    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    @Override
    public Optional<PasswordToken> getTokenByToken(String token) {
        for (PasswordToken t : passwordTokenRepository.findAll()) {
            if (t.getToken().equals(token)) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    @Override
    public void deleteToken(PasswordToken passToken) {
        passwordTokenRepository.delete(passToken);
    }

    @Override
    public void save(PasswordToken token) {
        passwordTokenRepository.save(token);
    }

    public PasswordToken validatePasswordResetToken(String token) {
        Optional<PasswordToken> pOpt = getTokenByToken(token);
        if (pOpt.isPresent()) {
            PasswordToken passToken = pOpt.get();
            if (passToken != null && !isTokenExpired(passToken)) {
                return pOpt.get();
            }
        }
        return null;
    }

    private boolean isTokenExpired(PasswordToken passToken) {
        final Calendar cal = Calendar.getInstance();
        if (passToken.getExpiryDate().before(cal.getTime())) {
            deleteToken(passToken);
            return true;
        }
        return false;
    }

    /***
     * https://neilmadden.blog/2018/08/30/moving-away-from-uuids/
     */
    @Override
    public String generateResetToken() {
        byte[] buffer = new byte[20];
        random.nextBytes(buffer);
        String token = encoder.encodeToString(buffer);
        while (getTokenByToken(token).isPresent()) {
            token = encoder.encodeToString(buffer);
        }
        return token;
    }

    @Override
    public void deleteTokenByUsername(String username) {
        for (PasswordToken pt : passwordTokenRepository.findAll()) {
            if(pt.getUser().getUsername().equals(username)) {
                deleteToken(pt);
                return;
            }
        }
    }

}
