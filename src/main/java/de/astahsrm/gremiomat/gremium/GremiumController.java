package de.astahsrm.gremiomat.gremium;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.server.ResponseStatusException;

import de.astahsrm.gremiomat.candidate.Candidate;
import de.astahsrm.gremiomat.candidate.CandidateService;
import de.astahsrm.gremiomat.candidate.answer.CandidateAnswer;
import de.astahsrm.gremiomat.mail.MailService;
import de.astahsrm.gremiomat.query.Query;
import de.astahsrm.gremiomat.query.QueryDto;
import de.astahsrm.gremiomat.query.QueryService;
import de.astahsrm.gremiomat.security.MgmtUser;
import de.astahsrm.gremiomat.security.MgmtUserService;

@Controller
@RequestMapping("/")
@SessionAttributes("userAnswers")
public class GremiumController {

    private static final String REDIRECT_HOME = "redirect:/";

    private static final String USER_ANSWERS = "userAnswers";

    private static final String GREMIUM = "gremium";

    enum QueryNav {
        NEXT, RESULTS, PREV, SKIP
    }

    @Autowired
    private GremiumService gremiumService;

    @Autowired
    private MgmtUserService mgmtUserService;

    @Autowired
    private MailService mailService;

    @ModelAttribute(USER_ANSWERS)
    public void initSession(Model m) {
        if (m.getAttribute(USER_ANSWERS) == null) {
            m.addAttribute(USER_ANSWERS, new HashMap<Query, Integer>());
        }
    }

    @GetMapping
    public String getGremien(Model m) {
        m.addAllAttributes(gremiumService.getGremienNavMap());
        return "home";
    }

    @GetMapping("/login")
    public String getLogin(@RequestParam(required = false) boolean error, Model m) {
        m.addAllAttributes(gremiumService.getGremienNavMap());
        m.addAttribute("error", error);
        return "login";
    }

    @GetMapping("/reset-password")
    public String getReset(Model m) {
        m.addAllAttributes(gremiumService.getGremienNavMap());
        return "password/forgot-password";
    }

    @PostMapping("/reset-password")
    public String postReset(HttpServletRequest request, @RequestParam("email") String userEmail) {
        Optional<MgmtUser> uOpt = mgmtUserService.findUserByEmail(userEmail);
        if (uOpt.isPresent()) {
            MgmtUser user = uOpt.get();
            mailService.sendResetPasswordMail(request.getLocale(), user);
        }
        return "redirect:/login?reset=0";
    }

    @GetMapping("/{abbr}")
    public String getGremiumInfo(@PathVariable String abbr, Model m) {
        Optional<Gremium> gremiumOptional = gremiumService.getGremiumByAbbr(abbr);
        if (gremiumOptional.isPresent()) {
            if (m.getAttribute(USER_ANSWERS) != null) {
                m.addAttribute(USER_ANSWERS, new HashMap<Query, Integer>());
            }
            m.addAttribute(GREMIUM, gremiumOptional.get());
            m.addAllAttributes(gremiumService.getGremienNavMap());
            return "gremien/info";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, GremiumService.GREMIUM_NOT_FOUND);
    }

    @GetMapping("/{abbr}/candidates/{id}")
    public String getCandidate(@PathVariable String abbr, @PathVariable long id, Model m) {
        Optional<Gremium> gremiumOptional = gremiumService.getGremiumByAbbr(abbr);
        if (gremiumOptional.isPresent()) {
            Gremium gremium = gremiumOptional.get();
            for (Candidate c : gremium.getJoinedCandidates()) {
                if (c.getId() == id) {
                    m.addAttribute("candidate", c);
                    m.addAttribute(GREMIUM, gremium);
                    m.addAllAttributes(gremiumService.getGremienNavMap());
                    return "gremien/candidate";
                }
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, CandidateService.CANDIDATE_NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, GremiumService.GREMIUM_NOT_FOUND);
    }

    @GetMapping("/{abbr}/queries/{queryIndex}")
    public String getQuery(@SessionAttribute HashMap<Query, Integer> userAnswers, @PathVariable String abbr,
            @PathVariable int queryIndex, Model m) {
        Optional<Gremium> gremiumOptional = gremiumService.getGremiumByAbbr(abbr);
        if (gremiumOptional.isPresent()) {
            Gremium gremium = gremiumOptional.get();
            if (gremium.getContainedQueries().size() > queryIndex) {
                QueryDto form = new QueryDto();
                Query query = gremium.getContainedQueries().get(queryIndex);
                if (userAnswers.size() > 0) {
                    for (Map.Entry<Query, Integer> entry : userAnswers.entrySet()) {
                        if (entry.getKey().equals(query)) {
                            form.setOpinion(entry.getValue());
                            break;
                        }
                    }
                }
                m.addAttribute(GREMIUM, gremium);
                m.addAttribute("queryListSize", gremium.getContainedQueries().size());
                m.addAttribute("query", query);
                m.addAttribute("queryIndex", queryIndex);
                m.addAttribute("queryForm", form);
                m.addAttribute("isQueriesAnswered", userAnswers.size() == gremium.getContainedQueries().size()
                        || queryIndex == gremium.getContainedQueries().size() - 1);
                m.addAllAttributes(gremiumService.getGremienNavMap());
                return "gremien/query";
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, QueryService.QUERY_NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, GremiumService.GREMIUM_NOT_FOUND);
    }

    @PostMapping(value = "/{abbr}/queries/{queryIndex}", params = "next")
    public String nextQueryPost(@SessionAttribute HashMap<Query, Integer> userAnswers, @Valid QueryDto form,
            @PathVariable String abbr, @PathVariable int queryIndex, Model m) {
        return handleQueryNav(QueryNav.NEXT, userAnswers, abbr, queryIndex, m, form);
    }

    @PostMapping(value = "/{abbr}/queries/{queryIndex}", params = "prev")
    public String prevQueryPost(@SessionAttribute HashMap<Query, Integer> userAnswers, @Valid QueryDto form,
            @PathVariable String abbr, @PathVariable int queryIndex, Model m) {
        return handleQueryNav(QueryNav.PREV, userAnswers, abbr, queryIndex, m, form);
    }

    @PostMapping(value = "/{abbr}/queries/{queryIndex}", params = "results")
    public String resultsPost(@SessionAttribute HashMap<Query, Integer> userAnswers, @Valid QueryDto form,
            @PathVariable String abbr, @PathVariable int queryIndex, Model m) {
        return handleQueryNav(QueryNav.RESULTS, userAnswers, abbr, queryIndex, m, form);
    }

    @PostMapping(value = "/{abbr}/queries/{queryIndex}", params = "skip")
    public String skipQueryPost(@SessionAttribute HashMap<Query, Integer> userAnswers, @Valid QueryDto form,
            @PathVariable String abbr, @PathVariable int queryIndex, Model m) {
        return handleQueryNav(QueryNav.SKIP, userAnswers, abbr, queryIndex, m, form);
    }

    @GetMapping("/{abbr}/queries/results")
    public String getResults(@SessionAttribute HashMap<Query, Integer> userAnswers, @PathVariable String abbr,
            Model m) {
        Optional<Gremium> gremiumOptional = gremiumService.getGremiumByAbbr(abbr);
        if (gremiumOptional.isPresent()) {
            Gremium gremium = gremiumOptional.get();
            HashMap<Candidate, Double> compatibility = new HashMap<>();
            int skipped = 0;
            // Goes through every candidate in gremium
            for (Candidate candidate : gremium.getJoinedCandidates()) {
                double percentage = 0;
                double answersInCommon = 0;
                // Goes through every entry inside of 'userAnswers' Map
                for (Map.Entry<Query, Integer> entry : userAnswers.entrySet()) {
                    /*
                     * Finds matching Query from 'userAnswers' Map and compares it to candidate's
                     * answer. Increments 'answersInCommon' if candidate's answer and user's answer
                     * are equal.
                     */
                    for (CandidateAnswer ans : candidate.getAnswers()) {
                        if (ans.getQuery().equals(entry.getKey())) {
                            if (entry.getValue() == ans.getOpinion()) {
                                answersInCommon++;
                            }
                            break;
                        }
                    }
                }
                percentage = (answersInCommon / userAnswers.size()) * 100;
                compatibility.put(candidate, Math.round(percentage * 100.0) / 100.0);
                m.addAttribute(GREMIUM, gremium);
            }
            for (Map.Entry<Query, Integer> entry : userAnswers.entrySet()) {
                if (entry.getValue() == 2) {
                    skipped++;
                }
            }
            LinkedHashMap<Candidate, Double> sortedByComp = compatibility.entrySet().stream()
                    .sorted((Map.Entry.<Candidate, Double>comparingByValue().reversed())).collect(Collectors
                            .toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
            m.addAttribute("comp", sortedByComp);
            m.addAttribute("skippedAnswers", skipped);
            m.addAllAttributes(gremiumService.getGremienNavMap());
            return "gremien/results";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, GremiumService.GREMIUM_NOT_FOUND);
    }

    @PostMapping("/{abbr}/queries/results")
    public String postResults(SessionStatus status) {
        status.setComplete();
        return REDIRECT_HOME;
    }

    private String handleQueryNav(QueryNav nav, HashMap<Query, Integer> userAnswers, String abbr, int queryIndex,
            Model m, @Valid QueryDto form) {
        String redirect = REDIRECT_HOME + abbr + "/queries/";
        Optional<Gremium> gremiumOptional = gremiumService.getGremiumByAbbr(abbr);
        if (gremiumOptional.isPresent()) {
            Gremium gremium = gremiumOptional.get();
            if (gremium.getContainedQueries().size() > queryIndex && queryIndex >= 0) {
                Query q = gremium.getContainedQueries().get(queryIndex);
                if (nav == QueryNav.SKIP) {
                    userAnswers.put(q, 2);
                    m.addAttribute(USER_ANSWERS, userAnswers);
                    if (queryIndex + 1 < gremium.getContainedQueries().size()) {
                        return redirect + Integer.toString(queryIndex + 1);
                    } else {
                        return redirect + "results";
                    }
                }
                userAnswers.put(q, form.getOpinion());
                m.addAttribute(USER_ANSWERS, userAnswers);
                switch (nav) {
                case NEXT:
                    return redirect + Integer.toString(queryIndex + 1);
                case PREV:
                    return redirect + Integer.toString(queryIndex - 1);
                case RESULTS:
                    return redirect + "results";
                default:
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, QueryService.QUERY_NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, GremiumService.GREMIUM_NOT_FOUND);
    }

}
