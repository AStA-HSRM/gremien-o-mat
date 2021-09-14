package de.astahsrm.gremiomat.gremium;

import java.util.List;
import java.util.Optional;

import de.astahsrm.gremiomat.candidate.Candidate;
import de.astahsrm.gremiomat.query.Query;
import javassist.NotFoundException;

public interface GremiumService {
    public Gremium saveGremium(Gremium gremium);
    public Optional<Gremium> getGremiumByAbbr(String gremiumAbbr);
    public void delGremium(String gremiumAbbr);

    public List<Gremium> getAllGremiumsSortedByName();
    public List<Query> getGremiumQueriesByGremiumAbbr(String gremiumAbbr) throws NotFoundException;
    public List<Candidate> getGremiumCandidatesByGremiumAbbr(String gremiumAbbr) throws NotFoundException;

}
