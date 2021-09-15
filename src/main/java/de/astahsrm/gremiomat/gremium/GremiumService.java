package de.astahsrm.gremiomat.gremium;

import java.util.List;
import java.util.Optional;

import de.astahsrm.gremiomat.candidate.Candidate;
import de.astahsrm.gremiomat.query.Query;
import javassist.NotFoundException;

public interface GremiumService {
    public Gremium saveGremium(Gremium gremium);
    public Optional<Gremium> getGremiumByAbbr(String abbr);
    public void delByAbbrGremium(String abbr);
    public List<Gremium> getAllGremiumsSortedByName();
    public void addCandidateToGremium(Candidate candidate, Gremium gremium);
    public List<Query> getGremiumQueriesByGremiumAbbr(String abbr) throws NotFoundException;
    public List<Candidate> getGremiumCandidatesByGremiumAbbr(String abbr) throws NotFoundException;

}
