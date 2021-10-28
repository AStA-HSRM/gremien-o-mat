package de.astahsrm.gremiomat.user;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import de.astahsrm.gremiomat.candidate.Candidate;
import de.astahsrm.gremiomat.candidate.CandidateDto;
import de.astahsrm.gremiomat.candidate.CandidateService;
import de.astahsrm.gremiomat.candidate.answer.CandidateAnswer;
import de.astahsrm.gremiomat.candidate.answer.CandidateAnswerDto;
import de.astahsrm.gremiomat.candidate.answer.CandidateAnswerService;
import de.astahsrm.gremiomat.faculty.Faculty;
import de.astahsrm.gremiomat.faculty.FacultyService;
import de.astahsrm.gremiomat.password.PasswordDto;
import de.astahsrm.gremiomat.password.PasswordTokenService;
import de.astahsrm.gremiomat.photo.Photo;
import de.astahsrm.gremiomat.photo.PhotoService;
import de.astahsrm.gremiomat.security.MgmtUser;
import de.astahsrm.gremiomat.security.MgmtUserService;
import de.astahsrm.gremiomat.security.SecurityConfig;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final String REDIRECT_USER = "redirect:/user";

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private CandidateAnswerService candidateAnswerService;

    @Autowired
    private MgmtUserService mgmtUserService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private PasswordTokenService passwordTokenService;

    @Autowired
    private FacultyService facultyService;

    @GetMapping
    public String getUserInfo(Principal loggedInUser, Model m) {
        Optional<MgmtUser> uOpt = mgmtUserService.getUserById(loggedInUser.getName());
        if (uOpt.isPresent()) {
            MgmtUser user = uOpt.get();
            if(user.getRole().equals(SecurityConfig.ADMIN)) {
                return "redirect:/admin/users";
            }
            Candidate userDetails = user.getDetails();
            CandidateDto form = new CandidateDto();
            form.setFirstname(userDetails.getFirstname());
            form.setLastname(userDetails.getLastname());
            form.setAge(userDetails.getAge());
            form.setCourse(userDetails.getCourse());
            form.setSemester(userDetails.getSemester());
            form.setBio(userDetails.getBio());
            form.setAgeShowing(userDetails.isAgeShowing());
            form.setCourseShowing(userDetails.isCourseShowing());
            if (userDetails.getFaculty() != null) {
                form.setFaculty(userDetails.getFaculty().getAbbr());
            }
            m.addAttribute("form", form);
            m.addAttribute("faculties", facultyService.getAllFaculties());
            m.addAttribute("candidate", userDetails);
            return "user/user-info-edit";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, MgmtUserService.USER_NOT_FOUND);
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

    @PostMapping("/info/edit")
    public String postUserInfoEditWithImage(Principal loggedInUser, @Valid CandidateDto form, BindingResult res,
            Model m) {
        if (res.hasErrors()) {
            return "user/user-info-edit";
        }
        Candidate c = mgmtUserService.getCandidateDetailsOfUser(loggedInUser.getName());
        c.setFirstname(form.getFirstname());
        c.setLastname(form.getLastname());
        c.setAge(form.getAge());
        c.setSemester(form.getSemester());
        c.setAgeShowing(form.isAgeShowing());
        c.setCourseShowing(form.isCourseShowing());
        c.setCourse(form.getCourse());
        c.setBio(form.getBio());
        Optional<Faculty> fOpt = facultyService.getByAbbr(form.getFaculty());
        if (fOpt.isPresent()) {
            c.setFaculty(fOpt.get());
        } else {
            c.setFaculty(null);
        }
        Optional<MgmtUser> uOpt = mgmtUserService.getUserById(loggedInUser.getName());
        if (uOpt.isPresent()) {
            MgmtUser u = uOpt.get();
            u.setDetails(candidateService.saveCandidate(c));
            mgmtUserService.saveUser(u);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, MgmtUserService.USER_NOT_FOUND);
        }
        return REDIRECT_USER;
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
            u.setDetails(candidateService.saveCandidate(c));
            mgmtUserService.saveUser(u);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, MgmtUserService.USER_NOT_FOUND);
        }
        return REDIRECT_USER;
    }

    @GetMapping("/info/upload/del")
    public String getUserInfoUploadDel(Principal loggedInUser, Model m) {
        Candidate c = mgmtUserService.getCandidateDetailsOfUser(loggedInUser.getName());
        Photo p = c.getPhoto();
        c.setPhoto(null);
        photoService.delPhoto(p);
        Optional<MgmtUser> uOpt = mgmtUserService.getUserById(loggedInUser.getName());
        if (uOpt.isPresent()) {
            MgmtUser u = uOpt.get();
            u.setDetails(candidateService.saveCandidate(c));
            mgmtUserService.saveUser(u);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, MgmtUserService.USER_NOT_FOUND);
        }
        return REDIRECT_USER;
    }

    @GetMapping("answers/{answerId}/edit")
    public String getAnswerEdit(@PathVariable long answerId, Model m) {
        Optional<CandidateAnswer> aOpt = candidateAnswerService.getAnswerById(answerId);
        if (aOpt.isPresent()) {
            CandidateAnswer ca = aOpt.get();
            CandidateAnswerDto form = new CandidateAnswerDto();
            form.setQuery(ca.getQuery());
            form.setAnswerId(ca.getId());
            form.setOpinion(ca.getOpinion());
            form.setReason(ca.getReason());
            m.addAttribute("form", form);
            m.addAttribute("query", ca.getQuery());
            return "user/answer-edit";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, CandidateAnswerService.ANSWER_NOT_FOUND);
    }

    @PostMapping("answers/{answerId}/edit")
    public String postAnswerEdit(@Valid CandidateAnswerDto form, @PathVariable long answerId, BindingResult res,
            Principal loggedInUser, Model m) {
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
                c.addNewAnswer(candidateAnswerService.saveAnswer(ca));
            }
            Optional<MgmtUser> uOpt = mgmtUserService.getUserById(loggedInUser.getName());
            if (uOpt.isPresent()) {
                MgmtUser u = uOpt.get();
                u.setDetails(candidateService.saveCandidate(c));
                mgmtUserService.saveUser(u);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, MgmtUserService.USER_NOT_FOUND);
            }
            return REDIRECT_USER;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, CandidateService.CANDIDATE_NOT_FOUND);
        }
    }
}
