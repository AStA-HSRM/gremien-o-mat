package de.astahsrm.gremiomat.gremium;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.server.ResponseStatusException;

import de.astahsrm.gremiomat.query.Query;
import de.astahsrm.gremiomat.query.QueryForm;
import de.astahsrm.gremiomat.query.QueryService;

@Controller
@RequestMapping("/gremien")
@SessionAttributes("userAnswers")
public class GremiumController {

    private static final String USER_ANSWERS = "userAnswers";

    @Autowired
    private GremiumService gremiumService;

    @Autowired
    private QueryService queryService;

    @ModelAttribute(USER_ANSWERS)
    public void initSession(Model m) {
        if (m.getAttribute(USER_ANSWERS) == null) {
            m.addAttribute(USER_ANSWERS, new HashMap<Query, Integer>());
        }
    }

    @GetMapping
    public String getGremien(Model m) {
        m.addAttribute("gremien", gremiumService.getAllGremiumsSortedByName());
        return "gremien/overview";
    }

    @GetMapping("/{abbr}")
    public String getGremiumInfo(@PathVariable String abbr, Model m) {
        Optional<Gremium> gremiumOptional = gremiumService.getGremiumByAbbr(abbr);
        if (gremiumOptional.isPresent()) {
            m.addAttribute("gremium", gremiumOptional.get());
            return "gremien/info";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, GremiumService.GREMIUM_NOT_FOUND);
    }

    @GetMapping("/{abbr}/{queryIndex}")
    public String getQuery(Model m, @PathVariable String abbr, @PathVariable int queryIndex) {
        Optional<Gremium> gremiumOptional = gremiumService.getGremiumByAbbr(abbr);
        if (gremiumOptional.isPresent()) {
            Gremium gremium = gremiumOptional.get();
            if (gremium.getContainedQueries().size() > queryIndex) {
                m.addAttribute("gremium", gremium);
                m.addAttribute("queryListSize", gremium.getContainedQueries().size());
                m.addAttribute("query", gremium.getContainedQueries().get(queryIndex));
                m.addAttribute("queryIndex", queryIndex);
                m.addAttribute("queryForm", new QueryForm());
                return "gremien/query";
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such Query exists!");
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, GremiumService.GREMIUM_NOT_FOUND);
    }

    @PostMapping(value = "/{abbr}/{queryIndex}", params = "next-query")
    public String nextQueryPost(@SessionAttribute HashMap<Query, Integer> userAnswers, @ModelAttribute QueryForm form,
            @PathVariable String abbr, @PathVariable int queryIndex, Model m) {
        Optional<Gremium> gremiumOptional = gremiumService.getGremiumByAbbr(abbr);
        if (gremiumOptional.isPresent()) {
            Gremium gremium = gremiumOptional.get();
            if (gremium.getContainedQueries().size() > queryIndex) {
                userAnswers.put(gremium.getContainedQueries().get(queryIndex), form.getOpinion());
                m.addAttribute(USER_ANSWERS, userAnswers);
                return "redirect:./" + Integer.toString(queryIndex + 1);
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such Query exists!");
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, GremiumService.GREMIUM_NOT_FOUND);
    }

    @PostMapping(value = "/{abbr}/{queryIndex}", params = "prev-query")
    public String prevQueryPost(@SessionAttribute HashMap<Query, Integer> userAnswers, @ModelAttribute QueryForm form,
            @PathVariable String abbr, @PathVariable int queryIndex, Model m) {
                Optional<Gremium> gremiumOptional = gremiumService.getGremiumByAbbr(abbr);
                if (gremiumOptional.isPresent()) {
                    Gremium gremium = gremiumOptional.get();
                    if (gremium.getContainedQueries().size() > queryIndex) {
                        userAnswers.put(gremium.getContainedQueries().get(queryIndex), form.getOpinion());
                        m.addAttribute(USER_ANSWERS, userAnswers);
                        return "redirect:./" + Integer.toString(queryIndex - 1);
                    }
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such Query exists!");
                }
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, GremiumService.GREMIUM_NOT_FOUND);
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
        q.add(queryService.saveQuery(q1));
        q.add(queryService.saveQuery(q2));
        gremium.setContainedQueries(q);
        gremiumService.saveGremium(gremium);
        return "redirect:/gremien";
    }

}
