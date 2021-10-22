package de.astahsrm.gremiomat.password;

import java.util.Optional;

public interface PasswordTokenService {
    public Optional<PasswordResetToken> getTokenByToken(String token);

    public void deleteToken(PasswordResetToken passToken);
}
