package de.astahsrm.gremiomat.user;

import java.security.Principal;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import de.astahsrm.gremiomat.candidate.Candidate;
import de.astahsrm.gremiomat.candidate.CandidateAnswer;
import de.astahsrm.gremiomat.candidate.CandidateAnswerForm;
import de.astahsrm.gremiomat.candidate.CandidateService;
import de.astahsrm.gremiomat.gremium.Gremium;
import de.astahsrm.gremiomat.query.Query;
import de.astahsrm.gremiomat.query.QueryService;
import de.astahsrm.gremiomat.security.MgmtUser;
import de.astahsrm.gremiomat.security.MgmtUserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private QueryService queryService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private MgmtUserService mgmtUserService;

    @GetMapping
    public String getUserPage() {
        return "user/user";
    }

    @GetMapping("/info")
    public String getUserInfoPage(Principal loggedInUser, Model m) {
        Candidate userDetails = mgmtUserService.getCandidateDetailsOfUser(loggedInUser.getName());
        m.addAttribute("candidate", userDetails);
        return "user/user-info";
    }

    @GetMapping("/info/edit")
    public String getUserInfoEditPage(Principal loggedInUser, Model m) {
        m.addAttribute("form", new Candidate());
        return "user/user-info-edit";
    }

    @GetMapping("/answers")
    public String getUserAnswersPage(Principal loggedInUser, Model m) {
        HashMap<Query, CandidateAnswer> queryAnswerMap = new HashMap<>();
        Candidate userDetails = mgmtUserService.getCandidateDetailsOfUser(loggedInUser.getName());
        for (Gremium g : userDetails.getGremien()) {
            for (Query q : g.getContainedQueries()) {
                queryAnswerMap.put(q, null);
                for (CandidateAnswer ans : userDetails.getAnswers()) {
                    if (ans.getQuery().equals(q)) {
                        queryAnswerMap.put(q, ans);
                        break;
                    }
                }
            }
        }
        m.addAttribute("queryAnswerMap", queryAnswerMap);
        return "user/answer-overview";
    }

    @GetMapping("answers/{queryId}/edit")
    public String getGremiumQueryEditPage(@PathVariable int queryId, Model m, Principal loggedInUser) {
        Optional<Query> qOpt = queryService.getQueryById(queryId);
        if (qOpt.isPresent()) {
            Query query = qOpt.get();
            CandidateAnswerForm form = new CandidateAnswerForm();
            form.setQuery(query);
            Candidate userDetails = mgmtUserService.getCandidateDetailsOfUser(loggedInUser.getName());
            Optional<CandidateAnswer> ansOpt = candidateService.getCandidateAnswerByQueryTxt(query.getText(),
                    userDetails.getEmail());
            if (ansOpt.isPresent()) {
                CandidateAnswer ans = ansOpt.get();
                form.setAnswerId(ans.getId());
                form.setOpinion(ans.getOpinion());
                form.setReason(ans.getReason());
            }
            m.addAttribute("form", form);
            m.addAttribute("query", query);
            m.addAttribute("role", "USER");
            return "user/answer-edit";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, QueryService.QUERY_NOT_FOUND);
    }

    @PostMapping("answers/{queryId}/edit")
    public String postGremiumAnswerEditPage(@ModelAttribute CandidateAnswerForm form, @PathVariable int queryId,
            BindingResult res, Principal loggedInUser, Model m) {
        if (res.hasErrors()) {
            return "user/answer-edit";
        }
        Candidate userDetails = mgmtUserService.getCandidateDetailsOfUser(loggedInUser.getName());
        Optional<Candidate> cOpt = candidateService.getCandidateById(userDetails.getEmail());
        if (cOpt.isPresent()) {
            Candidate c = cOpt.get();
            boolean ansExists = false;
            for (CandidateAnswer ans : c.getAnswers()) {
                if (ans.getId() == form.getAnswerId()) {
                    ansExists = true;
                    ans.setOpinion(form.getOpinion());
                    ans.setQuery(form.getQuery());
                    ans.setReason(form.getReason());
                    break;
                }
            }
            if (!ansExists) {
                CandidateAnswer ca = new CandidateAnswer();
                ca.setOpinion(form.getOpinion());
                ca.setQuery(form.getQuery());
                ca.setReason(form.getReason());
                c.addNewAnswer(ca);
            }
            Optional<MgmtUser> uOpt = mgmtUserService.getUserById(loggedInUser.getName());
            if (uOpt.isPresent()) {
                MgmtUser u = uOpt.get();
                u.setCandidateDetails(candidateService.saveCandidate(c));
                mgmtUserService.saveUser(u);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, MgmtUserService.USER_NOT_FOUND);
            }
            return "redirect:/user/answers";
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, CandidateService.CANDIDATE_NOT_FOUND);
        }
    }
}
