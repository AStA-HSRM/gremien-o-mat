package de.astahsrm.gremiomat.gremium;

import java.util.List;
import java.util.Optional;

public interface GremiumService {
    public Optional<Gremium> getGremiumById(Long id);

    public Optional<Gremium> getGremiumByAbbr(String abbr);

    public List<Gremium> getAllGremiumsSortedByName();

    public void delGremium(Long id);

    public Gremium saveGremium(Gremium gremium);

    // TODO Candidate service function

    // TODO Query service funcions
}
