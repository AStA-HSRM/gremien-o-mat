package de.astahsrm.gremiomat.gremium;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import de.astahsrm.gremiomat.candidate.Candidate;
import de.astahsrm.gremiomat.query.Query;

@Entity
public class Gremium {

    @Version
    private long version;

    @NotBlank(message = "{notEmpty}")
    private String name;

    @Id
    @NotBlank(message = "{notEmpty}")
    @Pattern(regexp = "^(?=.{1,16}$)[a-z]++(?:-[a-z]++)*+$")
    private String abbr;

    @NotBlank(message = "{notEmpty}")
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String description;

    @NotNull
    @ManyToMany(targetEntity = Query.class, cascade = CascadeType.PERSIST)
    @JoinTable(name = "queriesInGremium", joinColumns = @JoinColumn(name = "gremium_id"), inverseJoinColumns = @JoinColumn(name = "query_id"))
    private Set<Query> queries;

    @NotNull
    @ManyToMany(targetEntity = Candidate.class, cascade = CascadeType.PERSIST)
    @JoinTable(name = "candidatesInGremium", joinColumns = @JoinColumn(name = "gremium_id"), inverseJoinColumns = @JoinColumn(name = "candidate_id"))
    private Set<Candidate> candidates;

    public Gremium() {
        this.name = "";
        this.abbr = "";
        this.description = "";
    }

    @Override
    public String toString() {
        return "Gremium [abbr=" + abbr + ", containedQueries=" + queries + ", description=" + description
                + ", joinedCandidates=" + candidates + ", name=" + name + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((abbr == null) ? 0 : abbr.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Gremium other = (Gremium) obj;
        if (abbr == null) {
            if (other.abbr != null)
                return false;
        } else if (!abbr.equals(other.abbr))
            return false;
        return true;
    }

    public void addCandidate(Candidate candidate) {
        this.candidates.add(candidate);
    }

    public void delCandidate(Candidate candidate) {
        this.candidates.remove(candidate);
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Query> getQueries() {
        return queries;
    }

    public void setQueries(Set<Query> queries) {
        this.queries = queries;
    }

    public Set<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(Set<Candidate> candidates) {
        this.candidates = candidates;
    }

    public void addQuery(Query query) {
        if (!this.queries.contains(query)) {
            this.queries.add(query);
        }
    }

    public void delQuery(Query q) {
        this.queries.remove(q);
    }

}
