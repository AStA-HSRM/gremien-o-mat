package de.astahsrm.gremiomat.gremium;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;

@Entity
public class Query {
    @Id
    @GeneratedValue
    private long id;

    @Version
    private long version;

    @NotBlank
    private String text;

    @NotBlank
    @OneToMany
    private List<Gremium> gremien;

    public Query() {
        this.text = "";
        this.gremien = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Query [gremien=" + gremien + ", id=" + id + ", text=" + text + ", version=" + version + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
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
        Query other = (Query) obj;
        if (id != other.id)
            return false;
        return true;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Gremium> getGremien() {
        return gremien;
    }

    public void setGremien(List<Gremium> gremien) {
        this.gremien = gremien;
    }
}
