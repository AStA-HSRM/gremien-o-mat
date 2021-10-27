package de.astahsrm.gremiomat.query;

import java.io.IOException;
import java.util.Optional;

import com.opencsv.exceptions.CsvException;

import org.springframework.web.multipart.MultipartFile;

public interface QueryService {

    public static final String QUERY_NOT_FOUND = "No such Query exists!";

    public Query saveQuery(Query query);

    public Optional<Query> getQueryById(long queryId);

    public Optional<Query> getQueryByTxt(String queryTxt);

    public void delQueryById(long queryId);

    public void delQuery(Query q);

    public void saveQueriesFromCSV(MultipartFile csvFile, String gremiumAbbr) throws IOException, CsvException;
}
