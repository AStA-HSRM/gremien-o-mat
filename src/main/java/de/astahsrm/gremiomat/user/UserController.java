package de.astahsrm.gremiomat.user;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import de.astahsrm.gremiomat.candidate.Candidate;
import de.astahsrm.gremiomat.candidate.CandidateAnswer;
import de.astahsrm.gremiomat.candidate.CandidateService;
import de.astahsrm.gremiomat.gremium.Gremium;
import de.astahsrm.gremiomat.gremium.GremiumService;
import de.astahsrm.gremiomat.query.EditQueryForm;
import de.astahsrm.gremiomat.query.Query;
import de.astahsrm.gremiomat.query.QueryService;
import de.astahsrm.gremiomat.security.MgmtUserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private GremiumService gremiumService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private MgmtUserService mgmtUserService;

    @GetMapping
    public String getUserPage() {
        return "mgmt/user/user";
    }

    @GetMapping("/info")
    public String getUserInfoPage() {
        return "mgmt/user/info";
    }

    @GetMapping("/info/edit")
    public String getUserInfoEditPage() {
        return "mgmt/user-edit";
    }

    @GetMapping("/queries")
    public String getUserQueriesPage() {
        return "mgmt/user/query-overview";
    }

    @GetMapping("/queries/{queryindex}/edit")
    public String getUserQueriesEditPage() {
        return "mgmt/query-edit";
    }

    @GetMapping("/gremien/{abbr}/{queryIndex}/edit")
    public String getGremiumQueryEditPage(@PathVariable String abbr, @PathVariable int queryIndex, Model m,
            Principal loggedInUser) {
        Optional<Gremium> gremiumOptional = gremiumService.getGremiumByAbbr(abbr);
        if (gremiumOptional.isPresent()) {
            Gremium gremium = gremiumOptional.get();
            if (queryIndex < gremium.getContainedQueries().size()) {
                Query query = gremium.getContainedQueries().get(queryIndex);
                EditQueryForm form = new EditQueryForm();
                form.setGremien(query.getGremien());
                Candidate userDetails = mgmtUserService.getCandidateDetailsOfUser(loggedInUser.getName());
                Optional<CandidateAnswer> ansOpt = candidateService.getCandidateAnswerByQueryTxt(query.getText(),
                        userDetails.getEmail());
                if (ansOpt.isPresent()) {
                    CandidateAnswer ans = ansOpt.get();
                    form.setOpinion(ans.getChoice());
                    form.setReason(ans.getReason());
                }
                m.addAttribute("form", form);
                m.addAttribute("query", query);
                m.addAttribute("role", "USER");
                return "mgmt/user-query-edit";
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, QueryService.QUERY_NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, GremiumService.GREMIUM_NOT_FOUND);
    }
}
