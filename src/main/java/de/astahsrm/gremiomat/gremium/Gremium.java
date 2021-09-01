package de.astahsrm.gremiomat.gremium;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
public class Gremium {
    @Id
    @GeneratedValue
    private long id;

    @Version
    private long version;

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
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Query> queries;

    public Gremium() {
        this.name = "";
        this.abbr = "";
        this.description = "";
        this.queries = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Gremium [description=" + description + ", id=" + id + ", name=" + name + ", version=" + version + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + (int) (version ^ (version >>> 32));
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
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (version != other.version)
            return false;
        return true;
    }

    public List<Query> getQueries() {
        return queries;
    }

    public void setQueries(List<Query> queries) {
        this.queries = queries;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
