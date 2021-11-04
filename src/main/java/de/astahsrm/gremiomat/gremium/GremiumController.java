package de.astahsrm.gremiomat.gremium;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
import de.astahsrm.gremiomat.mgmt.MgmtUser;
import de.astahsrm.gremiomat.mgmt.MgmtUserService;
import de.astahsrm.gremiomat.password.PasswordDto;
import de.astahsrm.gremiomat.password.PasswordTokenService;
import de.astahsrm.gremiomat.query.Query;
import de.astahsrm.gremiomat.query.QueryDto;
import de.astahsrm.gremiomat.query.QueryService;

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
    private PasswordTokenService passwordTokenService;

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
            try {
                mailService.sendResetPasswordMail(request.getLocale(), user);
            } catch (NoSuchMessageException | MessagingException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Welcome Mail could not be sent!");
            }
        }
        return "redirect:/login?reset=0";
    }

    @GetMapping("/change-password")
    public String getChangePassword(HttpServletRequest request, @RequestParam("token") String token, Model m) {
        if (passwordTokenService.validatePasswordResetToken(token) != null) {
            m.addAttribute("form", new PasswordDto());
            return "password/change-password";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/change-password")
    public String postChangePassword(@RequestParam("token") String token, Model m, @Valid PasswordDto form,
            BindingResult res) {
        if (res.hasErrors()) {
            m.addAttribute("error", res.getAllErrors());
            m.addAttribute("form", new PasswordDto());
            return "password/change-password";
        }
        try {
            mgmtUserService.changePassword(token, form.getNewPassword());
            return "redirect:/login?reset=1";
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getExplanation());
        }
    }

    @GetMapping("/{abbr}")
    public String getGremiumInfo(@PathVariable String abbr, Model m) {
        Optional<Gremium> gremiumOptional = gremiumService.findGremiumByAbbr(abbr);
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
        Optional<Gremium> gremiumOptional = gremiumService.findGremiumByAbbr(abbr);
        if (gremiumOptional.isPresent()) {
            Gremium gremium = gremiumOptional.get();
            for (Candidate c : gremium.getCandidates()) {
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
        Optional<Gremium> gremiumOptional = gremiumService.findGremiumByAbbr(abbr);
        if (gremiumOptional.isPresent()) {
            Gremium gremium = gremiumOptional.get();
            if (gremium.getQueries().size() > queryIndex) {
                QueryDto form = new QueryDto();
                Query query = gremium.getQueries().toArray(new Query[0])[queryIndex];
                if (userAnswers.size() > 0) {
                    for (Map.Entry<Query, Integer> entry : userAnswers.entrySet()) {
                        if (entry.getKey().equals(query)) {
                            form.setOpinion(entry.getValue());
                            break;
                        }
                    }
                }
                m.addAttribute(GREMIUM, gremium);
                m.addAttribute("queryListSize", gremium.getQueries().size());
                m.addAttribute("query", query);
                m.addAttribute("queryIndex", queryIndex);
                m.addAttribute("queryForm", form);
                m.addAttribute("isQueriesAnswered", userAnswers.size() == gremium.getQueries().size()
                        || queryIndex == gremium.getQueries().size() - 1);
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
        Optional<Gremium> gremiumOptional = gremiumService.findGremiumByAbbr(abbr);
        if (gremiumOptional.isPresent()) {
            Gremium gremium = gremiumOptional.get();
            HashMap<Candidate, Double> compatibility = new HashMap<>();
            int skipped = 0;
            // Goes through every candidate in gremium
            for (Candidate candidate : gremium.getCandidates()) {
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
        Optional<Gremium> gremiumOptional = gremiumService.findGremiumByAbbr(abbr);
        if (gremiumOptional.isPresent()) {
            Gremium gremium = gremiumOptional.get();
            if (gremium.getQueries().size() > queryIndex && queryIndex >= 0) {
                Query q = gremium.getQueries().toArray(new Query[0])[queryIndex];
                if (nav == QueryNav.SKIP) {
                    userAnswers.put(q, 2);
                    m.addAttribute(USER_ANSWERS, userAnswers);
                    if (queryIndex + 1 < gremium.getQueries().size()) {
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
