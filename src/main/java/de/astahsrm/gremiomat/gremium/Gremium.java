package de.astahsrm.gremiomat.gremium;

import java.util.ArrayList;
import java.util.List;

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
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "query_contain", joinColumns = @JoinColumn(name = "gremium_id"), inverseJoinColumns = @JoinColumn(name = "query_id"))
    private List<Query> containedQueries;

    @NotNull
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "candidate_join", joinColumns = @JoinColumn(name = "gremium_id"), inverseJoinColumns = @JoinColumn(name = "candidate_id"))
    private List<Candidate> joinedCandidates;

    public Gremium() {
        this.name = "";
        this.abbr = "";
        this.description = "";
        this.containedQueries = new ArrayList<>();
        this.joinedCandidates = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Gremium [abbr=" + abbr + ", containedQueries=" + containedQueries + ", description=" + description
                + ", joinedCandidates=" + joinedCandidates + ", name=" + name + "]";
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
        if (!this.joinedCandidates.contains(candidate))
            this.joinedCandidates.add(candidate);
    }

    public void delCandidate(Candidate candidate) {
        this.joinedCandidates.remove(candidate);
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

    public List<Query> getContainedQueries() {
        return containedQueries;
    }

    public void setContainedQueries(List<Query> containedQueries) {
        this.containedQueries = containedQueries;
    }

    public List<Candidate> getJoinedCandidates() {
        return joinedCandidates;
    }

    public void setJoinedCandidates(List<Candidate> joinedCandidates) {
        this.joinedCandidates = joinedCandidates;
    }

    public void addQuery(Query query) {
        if (!this.containedQueries.contains(query))
            this.containedQueries.add(query);
    }

}
