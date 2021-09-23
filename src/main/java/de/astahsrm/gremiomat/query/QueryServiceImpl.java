package de.astahsrm.gremiomat.query;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.astahsrm.gremiomat.gremium.Gremium;
import de.astahsrm.gremiomat.gremium.GremiumService;

@Service
public class QueryServiceImpl implements QueryService{

    @Autowired
    private QueryRepository queryRepository;

    @Autowired
    private GremiumService gremiumService;

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

    @Override
    public void delQueryByIndexAndGremium(int queryIndex, String abbr) {
        Optional<Gremium> gremOpt = gremiumService.getGremiumByAbbr(abbr);
        if(gremOpt.isPresent()) {
            Query q = gremOpt.get().getContainedQueries().get(queryIndex);
            if(q != null) {
                delQueryByQueryTxt(q.getText());
            }
        }
    }
    
}
