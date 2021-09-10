package de.astahsrm.gremiomat.gremium;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import de.astahsrm.gremiomat.query.Query;
import de.astahsrm.gremiomat.query.QueryService;
@Controller
@RequestMapping("/gremien")
public class GremiumController {

    @Autowired
    private GremiumService gremiumService;

    @Autowired
    private QueryService queryService;

    @GetMapping
    public String getGremien(Model m) {
        m.addAttribute("gremien", gremiumService.getAllGremiumsSortedByName());
        return "gremien/overview";
    }

    @GetMapping("/{abbr}")
    public String getGremiumInfo(@PathVariable String abbr, Model m) {
        Optional<Gremium> gremiumOptional = gremiumService.getGremiumByAbbr(abbr);
        if(gremiumOptional.isPresent()) {
            m.addAttribute("gremium", gremiumOptional.get());
            return "gremien/info";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such Gremium exists!");
    }
    
    @GetMapping("/{abbr}/{queryIndex}")
    public String getQuery(Model m, @PathVariable String abbr, @PathVariable int queryIndex) {
        Optional<Gremium> gremiumOptional = gremiumService.getGremiumByAbbr(abbr);
        if(gremiumOptional.isPresent()) {
            Gremium gremium = gremiumOptional.get();
            if(gremium.getQueries().size() > queryIndex) {
                m.addAttribute("gremium", gremium);
                m.addAttribute("queryListSize", gremium.getQueries().size());
                m.addAttribute("query", gremium.getQueries().get(queryIndex));
                m.addAttribute("queryIndex", queryIndex);
                return "gremien/query";
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such Query exists!");
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such Gremium exists!");
    }

    @GetMapping("/add")
    public String addGremium(Model m) {
        Gremium gremium = new Gremium();
        gremium.setAbbr("tst");
        gremium.setDescription("test-description");
        gremium.setName("test-name");
        ArrayList<Query> q = new ArrayList<>();
        Query q1 = new Query();
        q1.setText("Die Kuh ist doof!");
        Query q2 = new Query();
        q2.setText("Das Pferd macht mäh!");
        queryService.saveQuery(q1);
        queryService.saveQuery(q2);
        q.add(q1);
        q.add(q2);
        gremium.setQueries(q);
        gremiumService.saveGremium(gremium);
        return "redirect:/gremien";
    }
    
}
