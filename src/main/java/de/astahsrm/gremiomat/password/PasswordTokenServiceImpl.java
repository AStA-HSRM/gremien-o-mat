package de.astahsrm.gremiomat.password;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordTokenServiceImpl implements PasswordTokenService {

    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    @Override
    public Optional<PasswordResetToken> getTokenByToken(String token) {
        for (PasswordResetToken t : passwordTokenRepository.findAll()) {
            if (t.getToken().equals(token)) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    @Override
    public void deleteToken(PasswordResetToken passToken) {
       passwordTokenRepository.delete(passToken);
    }

    @Override
    public void save(PasswordResetToken token) {
       passwordTokenRepository.save(token);
    }

}
