package de.astahsrm.gremiomat.security;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.astahsrm.gremiomat.password.PasswordResetToken;
import de.astahsrm.gremiomat.password.PasswordTokenService;

@Service
public class SecurityServiceImpl implements SecurityService {

    private static final SecureRandom random = new SecureRandom();

    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    @Autowired
    private PasswordTokenService passwordTokenService;

    public PasswordResetToken validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> pOpt = passwordTokenService.getTokenByToken(token);
        if (pOpt.isPresent()) {
            PasswordResetToken passToken = pOpt.get();
            if (passToken != null && !isTokenExpired(passToken)) {
                return pOpt.get();
            }
        }
        return null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        if(passToken.getExpiryDate().before(cal.getTime())) {
            passwordTokenService.deleteToken(passToken);
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
        while(passwordTokenService.getTokenByToken(token).isPresent()) {
            token = encoder.encodeToString(buffer);
        }
        return token;
    }

    public String generatePassword() {
        String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(2);
        String specialChar = RandomStringUtils.random(2, 33, 47, false, false);
        String totalChars = RandomStringUtils.randomAlphanumeric(2);
        String combinedChars = upperCaseLetters.concat(lowerCaseLetters).concat(numbers).concat(specialChar)
                .concat(totalChars);
        List<Character> pwdChars = combinedChars.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        return pwdChars.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
    }

}
