package de.astahsrm.gremiomat.query;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryServiceImpl implements QueryService{

    @Autowired
    private QueryRepository queryRepository;

    @Override
    public Query saveQuery(Query query) {
        return queryRepository.save(query);
    }

    @Override
    public Optional<Query> getQueryByQueryTxt(String queryTxt) {
        return queryRepository.findById(queryTxt);
    }

    @Override
    public void delQueryByQueryTxt(String queryTxt) {
       queryRepository.deleteById(queryTxt);
    }
    
}
