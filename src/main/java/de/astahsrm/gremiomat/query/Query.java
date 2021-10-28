package de.astahsrm.gremiomat.query;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import de.astahsrm.gremiomat.gremium.Gremium;

@Entity
public class Query {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Version
    private long version;

    @NotBlank
    private String text;

    @NotNull
    @ManyToMany(targetEntity = Gremium.class, mappedBy = "queries", cascade = CascadeType.PERSIST)
    private Set<Gremium> gremien;

    public Query() {
        this.text = "";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Query other = (Query) obj;
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Query [text=" + text + "]";
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<Gremium> getGremien() {
        return gremien;
    }

    public void setGremien(Set<Gremium> gremien) {
        this.gremien = gremien;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean addGremium(Gremium gremium) {
        if (!this.gremien.contains(gremium))
            return this.gremien.add(gremium);
        else
            return false;
    }
}
