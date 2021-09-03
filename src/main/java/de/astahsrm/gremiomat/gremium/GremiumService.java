package de.astahsrm.gremiomat.gremium;

import java.util.List;
import java.util.Optional;

import de.astahsrm.gremiomat.candidate.Candidate;

public interface GremiumService {
    public Optional<Gremium> getGremiumById(Long id);

    public Optional<Gremium> getGremiumByAbbr(String abbr);

    public List<Gremium> getAllGremiumsSortedByName();

    public void delGremium(Long id);

    public Gremium saveGremium(Gremium gremium);

    public List<Candidate> getGremiumCandidatesById(Long gremiumId);

    public List<Query> getGremiumQueriesById(Long gremiumId);
}
