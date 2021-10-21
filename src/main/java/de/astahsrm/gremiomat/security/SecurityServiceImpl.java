package de.astahsrm.gremiomat.security;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import de.astahsrm.gremiomat.password.PasswordResetToken;
import de.astahsrm.gremiomat.password.PasswordTokenRepository;

public class SecurityServiceImpl implements SecurityService {

    private static final SecureRandom random = new SecureRandom();

    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public MgmtUser validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
        if (passToken != null && !isTokenExpired(passToken)) {
            MgmtUser user = passToken.getUser();
            UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(user, passwordEncoder.encode(user.getPassword()));
            Authentication auth = authenticationManager.authenticate(authReq);
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);
            return user;
        } else {
            return null;
        }
    
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }

    @Override
    public String generateResetToken() {
        byte[] buffer = new byte[20];
        random.nextBytes(buffer);
        return encoder.encodeToString(buffer);
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
