package de.astahsrm.gremiomat.password;

import java.util.Optional;

public interface PasswordTokenService {
    public Optional<PasswordToken> getTokenByToken(String token);

    public void deleteToken(PasswordToken passToken);

    public void save(PasswordToken myToken);

    public PasswordToken validatePasswordResetToken(String token);

    public String generateResetToken();

    public void deleteTokenByUsername(String username);
}
