package de.astahsrm.gremiomat.mgmt;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MgmtUserRepository extends JpaRepository<MgmtUser, String> {
    
}
