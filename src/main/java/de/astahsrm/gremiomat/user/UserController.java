package de.astahsrm.gremiomat.user;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import de.astahsrm.gremiomat.candidate.Candidate;
import de.astahsrm.gremiomat.candidate.CandidateAnswer;
import de.astahsrm.gremiomat.candidate.CandidateAnswerForm;
import de.astahsrm.gremiomat.candidate.CandidateForm;
import de.astahsrm.gremiomat.candidate.CandidateService;
import de.astahsrm.gremiomat.gremium.Gremium;
import de.astahsrm.gremiomat.photo.Photo;
import de.astahsrm.gremiomat.photo.PhotoService;
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

    @Autowired
    private PhotoService photoService;

    @GetMapping
    public String getUserPage() {
        return "user/user";
    }

    @GetMapping("/info")
    public String getUserInfo(Principal loggedInUser, Model m) {
        Candidate userDetails = mgmtUserService.getCandidateDetailsOfUser(loggedInUser.getName());
        if (userDetails.getPhoto() != null) {
            m.addAttribute("photoId", userDetails.getPhoto().getId());
        }
        m.addAttribute("candidate", userDetails);
        return "user/user-info";
    }

    @GetMapping("/info/edit")
    public String getUserInfoEdit(Principal loggedInUser, Model m) {
        Candidate userDetails = mgmtUserService.getCandidateDetailsOfUser(loggedInUser.getName());
        CandidateForm form = new CandidateForm();
        form.setFirstname(userDetails.getFirstname());
        form.setLastname(userDetails.getLastname());
        form.setAge(userDetails.getAge());
        form.setCourse(userDetails.getCourse());
        form.setSemester(userDetails.getSemester());
        form.setBio(userDetails.getBio());
        m.addAttribute("form", form);
        if (userDetails.getPhoto() != null) {
            m.addAttribute("photoId", userDetails.getPhoto().getId());
        }
        return "user/user-info-edit";
    }

    @PostMapping("/info/edit")
    public String postUserInfoEditWithImage(Principal loggedInUser, @ModelAttribute CandidateForm form,
            BindingResult res, Model m) {
        if (res.hasErrors()) {
            return "user/user-info-edit";
        }
        Candidate c = mgmtUserService.getCandidateDetailsOfUser(loggedInUser.getName());
        c.setFirstname(form.getFirstname());
        c.setLastname(form.getLastname());
        c.setAge(form.getAge());
        c.setSemester(form.getSemester());
        c.setCourse(form.getCourse());
        c.setBio(form.getBio());
        Optional<MgmtUser> uOpt = mgmtUserService.getUserById(loggedInUser.getName());
        if (uOpt.isPresent()) {
            MgmtUser u = uOpt.get();
            u.setCandidateDetails(candidateService.saveCandidate(c));
            mgmtUserService.saveUser(u);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, MgmtUserService.USER_NOT_FOUND);
        }
        return "redirect:/user/info";
    }

    @GetMapping("/info/upload")
    public String getUserInfoUpload(Principal loggedInUser, Model m) {
        Candidate userDetails = mgmtUserService.getCandidateDetailsOfUser(loggedInUser.getName());
        if (userDetails.getPhoto() != null) {
            m.addAttribute("photoId", userDetails.getPhoto().getId());
        }
        return "user/user-info-upload";
    }

    @PostMapping("/info/upload")
    public String uploadImage(Principal loggedInUser, @RequestParam("photo") MultipartFile file, Model m)
            throws IOException {
        Candidate c = mgmtUserService.getCandidateDetailsOfUser(loggedInUser.getName());
        Photo photo = new Photo();
        photo.setFilename(file.getOriginalFilename());
        photo.setMimeType(file.getContentType());
        photo.setBytes(file.getBytes());
        if (photo.getBytes().length >= 17) {
            c.setPhoto(photoService.save(photo));
        }
        Optional<MgmtUser> uOpt = mgmtUserService.getUserById(loggedInUser.getName());
        if (uOpt.isPresent()) {
            MgmtUser u = uOpt.get();
            u.setCandidateDetails(candidateService.saveCandidate(c));
            mgmtUserService.saveUser(u);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, MgmtUserService.USER_NOT_FOUND);
        }
        return "redirect:/user/info/edit";
    }

    @GetMapping("/answers")
    public String getUserAnswers(Principal loggedInUser, Model m) {
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
    public String getGremiumQueryEdit(@PathVariable int queryId, Model m, Principal loggedInUser) {
        Optional<Query> qOpt = queryService.getQueryById(queryId);
        if (qOpt.isPresent()) {
            Query query = qOpt.get();
            CandidateAnswerForm form = new CandidateAnswerForm();
            form.setQuery(query);
            Candidate userDetails = mgmtUserService.getCandidateDetailsOfUser(loggedInUser.getName());
            Optional<CandidateAnswer> ansOpt = candidateService.getCandidateAnswerByQueryTxt(query.getText(),
                    userDetails.getId());
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
    public String postGremiumAnswerEdit(@ModelAttribute CandidateAnswerForm form, @PathVariable int queryId,
            BindingResult res, Principal loggedInUser, Model m) {
        if (res.hasErrors()) {
            return "user/answer-edit";
        }
        Candidate userDetails = mgmtUserService.getCandidateDetailsOfUser(loggedInUser.getName());
        Optional<Candidate> cOpt = candidateService.getCandidateById(userDetails.getId());
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
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, CandidateService.CANDIDATE_NOT_FOUND);
        }
    }
}
