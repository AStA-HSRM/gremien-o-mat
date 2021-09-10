package de.astahsrm.gremiomat.query;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import de.astahsrm.gremiomat.gremium.Gremium;

@Entity
public class Query {

    @Version
    private long version;

    @Id
    @NotBlank
    private String text;

    @NotNull
    @ManyToMany(mappedBy = "containedQueries", cascade= CascadeType.PERSIST)
    private List<Gremium> gremien;

    public Query() {
        this.text = "";
        this.gremien = new ArrayList<>();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((text == null) ? 0 : text.hashCode());
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
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        return true;
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

    public List<Gremium> getGremien() {
        return gremien;
    }

    public void setGremien(List<Gremium> gremien) {
        this.gremien = gremien;
    }
}
