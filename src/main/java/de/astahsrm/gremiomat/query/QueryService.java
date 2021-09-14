package de.astahsrm.gremiomat.query;

import java.util.Optional;

public interface QueryService {
    public Query saveQuery(Query query);
    public Optional<Query> getQueryByQueryTxt(String queryTxt);
    public void delQueryByQueryTxt(String queryTxt);
}
