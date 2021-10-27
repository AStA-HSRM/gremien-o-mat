package de.astahsrm.gremiomat.password;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordTokenRepository extends JpaRepository<PasswordToken, Long> {

    PasswordToken findByToken(String token);
    
}
