package de.astahsrm.gremiomat.query;

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
    
}
