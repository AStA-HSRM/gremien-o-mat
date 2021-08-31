package de.astahsrm.gremiomat.gremium;

import java.util.Optional;

public interface GremiumService {
    public Optional<Gremium> getGremiumById(Long id);
    public void delGremium(Long id);
    public Gremium saveGremium(Gremium gremium);
}
