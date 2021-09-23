package de.astahsrm.gremiomat.query;

import java.util.Optional;

public interface QueryService {
    public static final String QUERY_NOT_FOUND = "No such Query exists!";
    public Query saveQuery(Query query);
    public Optional<Query> getQueryByQueryTxt(String queryTxt);
    public void delQueryByQueryTxt(String queryTxt);
    public void delQueryByIndexAndGremium(int queryIndex, String abbr);
}
