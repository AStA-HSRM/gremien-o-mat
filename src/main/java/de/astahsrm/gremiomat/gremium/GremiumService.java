package de.astahsrm.gremiomat.gremium;

import java.util.List;
import java.util.Optional;

public interface GremiumService {
    public Optional<Gremium> getGremiumById(Long id);

    public List<Gremium> getAllGremiumsSortedByName();

    public void delGremium(Long id);

    public Gremium saveGremium(Gremium gremium);
}
