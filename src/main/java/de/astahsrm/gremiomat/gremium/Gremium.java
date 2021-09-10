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

    @Id
    @NotBlank(message = "{notEmpty}")
    private String name;

    @NotBlank(message = "{notEmpty}")
    @Pattern(regexp = "^(?=.{1,16}$)[a-z]++(?:-[a-z]++)*+$")
    private String abbr;

    @NotBlank(message = "{notEmpty}")
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String description;

    @NotNull
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "query_contain", 
        joinColumns = @JoinColumn(name = "gremium_id"), 
        inverseJoinColumns = @JoinColumn(name = "query_id"))
    private List<Query> containedQueries;

    @NotNull
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "candidate_join",
        joinColumns = @JoinColumn(name = "gremium_id"), 
        inverseJoinColumns = @JoinColumn(name = "candidate_id"))
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
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    public List<Query> getQueries() {
        return containedQueries;
    }

    public List<Candidate> getCandidates() {
        return joinedCandidates;
    }

    public void setQueries(List<Query> queries) {
        this.containedQueries = queries;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
