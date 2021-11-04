package de.astahsrm.gremiomat.admin;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.opencsv.exceptions.CsvException;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import de.astahsrm.gremiomat.candidate.Candidate;
import de.astahsrm.gremiomat.candidate.CandidateDto;
import de.astahsrm.gremiomat.candidate.CandidateService;
import de.astahsrm.gremiomat.faculty.Faculty;
import de.astahsrm.gremiomat.faculty.FacultyService;
import de.astahsrm.gremiomat.gremium.Gremium;
import de.astahsrm.gremiomat.gremium.GremiumDto;
import de.astahsrm.gremiomat.gremium.GremiumService;
import de.astahsrm.gremiomat.mail.MailService;
import de.astahsrm.gremiomat.mgmt.MgmtUser;
import de.astahsrm.gremiomat.mgmt.MgmtUserDto;
import de.astahsrm.gremiomat.mgmt.MgmtUserService;
import de.astahsrm.gremiomat.photo.Photo;
import de.astahsrm.gremiomat.photo.PhotoService;
import de.astahsrm.gremiomat.query.Query;
import de.astahsrm.gremiomat.query.QueryAdminDto;
import de.astahsrm.gremiomat.query.QueryService;
import de.astahsrm.gremiomat.security.SecurityConfig;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final String ALL_GREMIEN = "allGremien";

    private static final String ADMIN_GREMIUM = "admin/gremium";

    private static final String ADMIN_GREMIEN = "admin/gremien";

    private static final String ADMIN_QUERY = "admin/query";

    private static final String ADMIN_QUERIES = "admin/queries";

    private static final String ADMIN_USER = "admin/user";

    private static final String ADMIN_USERS = "admin/users";

    private static final String REDIRECT = "redirect:/";

    private static final String REDIRECT_ADMIN_GREMIEN = REDIRECT + ADMIN_GREMIEN;

    private static final String REDIRECT_ADMIN_QUERIES = REDIRECT + ADMIN_QUERIES;

    private static final String FORWARD_ADMIN_QUERIES = "forward:/" + ADMIN_QUERIES;

    private static final String REDIRECT_ADMIN_USERS = REDIRECT + ADMIN_USERS;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private GremiumService gremiumService;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private QueryService queryService;

    @Autowired
    private MgmtUserService mgmtUserService;

    @Autowired
    private PhotoService photoService;

    @GetMapping
    public String getAdminPage() {
        return "admin/admin";
    }

    @GetMapping("/gremien")
    public String getGremien(Model m) {
        m.addAllAttributes(gremiumService.getGremienNavMap());
        m.addAttribute("form", new GremiumDto());
        return ADMIN_GREMIEN;
    }

    @GetMapping("/gremien/{abbr}/del")
    public String delGremium(@PathVariable String abbr) {
        gremiumService.delByAbbrGremium(abbr);
        return REDIRECT_ADMIN_GREMIEN;
    }

    @PostMapping("/gremien/new")
    public String saveNewGremiumString(GremiumDto form, BindingResult res, Model m) {
        if (res.hasErrors()) {
            m.addAttribute("errors", res.getAllErrors());
        } else {
            Gremium gremium = new Gremium();
            gremium.setName(form.getName());
            gremium.setAbbr(form.getAbbr());
            gremium.setDescription(form.getDescription());
            gremiumService.saveGremium(gremium);
        }
        return REDIRECT_ADMIN_GREMIEN;
    }

    @GetMapping("/gremien/{abbr}")
    public String getGremiumInfoPage(@PathVariable String abbr, Model m) {
        Optional<Gremium> gOpt = gremiumService.findGremiumByAbbr(abbr);
        if (gOpt.isPresent()) {
            Gremium g = gOpt.get();
            GremiumDto form = new GremiumDto();
            form.setName(g.getName());
            form.setDescription(g.getDescription());
            m.addAttribute("form", form);
            m.addAttribute("gremium", g);
            return ADMIN_GREMIUM;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, GremiumService.GREMIUM_NOT_FOUND);
    }

    @PostMapping("/gremien/{abbr}")
    public String postGremiumEditPage(@PathVariable String abbr, GremiumDto form, BindingResult res, Model m) {
        if (res.hasErrors()) {
            m.addAttribute("errors", res.getAllErrors());
            return ADMIN_GREMIUM;
        }
        Optional<Gremium> gOpt = gremiumService.findGremiumByAbbr(abbr);
        if (gOpt.isPresent()) {
            Gremium g = gOpt.get();
            g.setName(form.getName());
            g.setDescription(form.getDescription());
            gremiumService.saveGremium(g);
            return REDIRECT_ADMIN_GREMIEN + "/" + abbr;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, GremiumService.GREMIUM_NOT_FOUND);
    }

    @GetMapping("/gremien/{abbr}/queries/new")
    public String newQuery(Model m, @PathVariable String abbr) {
        m.addAttribute(ALL_GREMIEN, gremiumService.getAllGremiumsSortedByName());
        m.addAttribute("form", new QueryAdminDto());
        m.addAttribute("abbr", abbr);
        return ADMIN_QUERY;
    }

    @GetMapping("/gremien/{abbr}/queries/{id}")
    public String getGremiumQueryEditPage(@PathVariable String abbr, @PathVariable long id, Model m) {
        Optional<Query> qOpt = queryService.getQueryById(id);
        if (qOpt.isPresent()) {
            Query query = qOpt.get();
            QueryAdminDto form = new QueryAdminDto();
            form.setTxt(query.getText());
            for (Gremium g : query.getGremien()) {
                form.addGremium(g);
            }
            m.addAttribute(ALL_GREMIEN, gremiumService.getAllGremiumsSortedByName());
            m.addAttribute("form", form);
            m.addAttribute("query", query);
            m.addAttribute("abbr", abbr);
            return ADMIN_QUERY;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, QueryService.QUERY_NOT_FOUND);
    }

    @GetMapping("/users")
    public String getUserOverview(Model m) {
        m.addAttribute("allUsers", mgmtUserService.getAllUsersSortedByUsername());
        m.addAttribute(ALL_GREMIEN, gremiumService.getAllGremiumsSortedByName());
        m.addAttribute("usersLocked", mgmtUserService.areUsersLocked());
        m.addAttribute("form", new MgmtUserDto());
        return ADMIN_USERS;
    }

    @PostMapping("/users/new")
    public String postNewUser(HttpServletRequest request, @ModelAttribute MgmtUserDto form, BindingResult res,
            Model m) {
        if (res.hasErrors()) {
            return ADMIN_USER;
        }
        if (form.getRole().equals(SecurityConfig.ADMIN)) {
            try {
                mgmtUserService.saveNewAdmin(request.getLocale(), form.getFirstname(), form.getLastname(),
                        form.getEmail());

            } catch (NoSuchMessageException | MessagingException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MailService.MAIL_ERROR);
            }
        } else {
            Candidate c = new Candidate();
            c.setFirstname(form.getFirstname());
            c.setLastname(form.getLastname());
            if (!candidateService.candidateExists(c)) {
                try {
                    mgmtUserService.saveNewUser(form.getEmail(), candidateService.saveCandidate(c),
                            request.getLocale());
                } catch (NoSuchMessageException | MessagingException e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MailService.MAIL_ERROR);
                }
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MgmtUserService.USER_NOT_FOUND);
            }
        }
        return REDIRECT_ADMIN_USERS + "?sent=true";
    }

    @GetMapping("users/{username}")
    public String getUser(Model m, @PathVariable String username) {
        Optional<MgmtUser> uOpt = mgmtUserService.getUserById(username);
        if (uOpt.isPresent()) {
            MgmtUser user = uOpt.get();
            if(!user.hasDetails()) {
                return REDIRECT_ADMIN_USERS;
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
            form.setGremienWithGremien(userDetails.getGremien());
            if (userDetails.getFaculty() != null) {
                form.setFaculty(userDetails.getFaculty().getAbbr());
            }
            m.addAttribute("form", form);
            m.addAttribute("user", user);
            m.addAttribute("username", user.getUsername());
            m.addAttribute("faculties", facultyService.getAllFaculties());
            m.addAttribute(ALL_GREMIEN, gremiumService.getAllGremiumsSortedByName());
            m.addAttribute("candidate", userDetails);
            return ADMIN_USER;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, MgmtUserService.USER_NOT_FOUND);
    }

    @PostMapping("users/{username}")
    public String postUserUpdate(@PathVariable String username, @Valid CandidateDto form, BindingResult res, Model m) {
        if (res.hasErrors()) {
            return ADMIN_USER;
        }
        Candidate c = mgmtUserService.getCandidateDetailsOfUser(username);
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
        Optional<MgmtUser> uOpt = mgmtUserService.getUserById(username);
        if (uOpt.isPresent()) {
            MgmtUser u = uOpt.get();
            u.setDetails(candidateService.saveCandidate(c));
            mgmtUserService.saveUser(u);
            return REDIRECT_ADMIN_USERS + "/" + u.getUsername();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, MgmtUserService.USER_NOT_FOUND);
        }
    }

    @PostMapping("/users/{username}/upload")
    public String uploadImage(@PathVariable String username, @RequestParam("photo") MultipartFile file, Model m)
            throws IOException {
        Candidate c = mgmtUserService.getCandidateDetailsOfUser(username);
        Photo photo = new Photo();
        photo.setFilename(file.getOriginalFilename());
        photo.setMimeType(file.getContentType());
        photo.setBytes(file.getBytes());
        if (photo.getBytes().length >= 17) {
            c.setPhoto(photoService.save(photo));
        }
        Optional<MgmtUser> uOpt = mgmtUserService.getUserById(username);
        if (uOpt.isPresent()) {
            MgmtUser u = uOpt.get();
            u.setDetails(candidateService.saveCandidate(c));
            mgmtUserService.saveUser(u);
            return REDIRECT_ADMIN_USERS + "/" + u.getUsername();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, MgmtUserService.USER_NOT_FOUND);
        }
    }

    @GetMapping("/users/{username}/upload/del")
    public String getUserInfoUploadDel(@PathVariable String username, Model m) {
        Optional<MgmtUser> uOpt = mgmtUserService.getUserById(username);
        if (uOpt.isPresent() && uOpt.get().hasDetails()) {
            MgmtUser u = uOpt.get();
            Photo p = u.getDetails().getPhoto();
            u.getDetails().setPhoto(null);
            photoService.delPhoto(p);
            mgmtUserService.saveUser(u);
            return REDIRECT_ADMIN_USERS + "/" + u.getUsername();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, MgmtUserService.USER_NOT_FOUND);
        }
    }

    @GetMapping("/users/{username}/del")
    public String delUser(Principal loggedInUser, @PathVariable String username, Model m) {
        if(loggedInUser.getName().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        mgmtUserService.delUserById(username);
        return REDIRECT_ADMIN_USERS;
    }

    @PostMapping("/users/csv")
    public String userCsvUpload(HttpServletRequest req, @RequestParam MultipartFile file,
            @RequestParam(required = false) String abbr) throws IOException, CsvException {
        try {
            mgmtUserService.saveUsersFromCSV(file, abbr, req.getLocale());
        } catch (NoSuchMessageException | MessagingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MailService.MAIL_ERROR);
        }
        return REDIRECT_ADMIN_USERS + "?sent=true";
    }

    @GetMapping("/users/lock")
    public String lockUser(Model m) {
        if (mgmtUserService.areUsersLocked()) {
            mgmtUserService.unlockAllUsers();
        } else {
            mgmtUserService.lockAllUsers();
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/queries")
    public String getQueries(Model m) {
        m.addAttribute("allQueries", queryService.getAllQueries());
        m.addAttribute(ALL_GREMIEN, gremiumService.getAllGremiumsSortedByName());
        return ADMIN_QUERIES;
    }

    @GetMapping("/queries/new")
    public String getNewQuery(Model m) {
        m.addAttribute(ALL_GREMIEN, gremiumService.getAllGremiumsSortedByName());
        m.addAttribute("form", new QueryAdminDto());
        return ADMIN_QUERY;
    }

    @GetMapping("/queries/{id}")
    public String getQuery(Model m, @PathVariable long id) {
        Optional<Query> qOpt = queryService.getQueryById(id);
        if (qOpt.isPresent()) {
            Query query = qOpt.get();
            QueryAdminDto form = new QueryAdminDto();
            form.setTxt(query.getText());
            for (Gremium g : query.getGremien()) {
                form.addGremium(g);
            }
            m.addAttribute(ALL_GREMIEN, gremiumService.getAllGremiumsSortedByName());
            m.addAttribute("form", form);
            m.addAttribute("query", query);
            return ADMIN_QUERY;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, QueryService.QUERY_NOT_FOUND);
    }

    @PostMapping("/queries/new")
    public String newQuery(Model m, QueryAdminDto form, BindingResult res,
            @RequestParam(required = false) String abbr) {
        if (res.hasErrors()) {
            m.addAttribute("error", res.getAllErrors());
            return ADMIN_QUERY;
        }
        Query q = new Query();
        if (abbr != null && !abbr.isBlank()) {
            Optional<Gremium> gOpt = gremiumService.findGremiumByAbbr(abbr);
            if (gOpt.isPresent()) {
                q.addGremium(gOpt.get());
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, GremiumService.GREMIUM_NOT_FOUND);
            }
        } else {
            q.setGremien(new HashSet<>());
        }
        q.setText(form.getTxt());
        queryService.saveQuery(q);
        if (abbr != null && !abbr.isBlank()) {
            return REDIRECT_ADMIN_GREMIEN + "/" + abbr;
        } else {
            return REDIRECT_ADMIN_QUERIES;
        }
    }

    @PostMapping("/queries/{id}")
    public String updateQuery(Model m, QueryAdminDto form, BindingResult res, @PathVariable long id,
            @RequestParam(required = false) String abbr) {
        if (res.hasErrors()) {
            m.addAttribute("error", res.getAllErrors());
            return ADMIN_QUERY;
        }
        Optional<Query> qOpt = queryService.getQueryById(id);
        if (qOpt.isPresent()) {
            Query q = qOpt.get();
            ArrayList<Gremium> gremien = new ArrayList<>();
            for (String gId : form.getGremien()) {
                Optional<Gremium> gOpt = gremiumService.findGremiumByAbbr(gId);
                if (gOpt.isPresent()) {
                    gremien.add(gOpt.get());
                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, GremiumService.GREMIUM_NOT_FOUND);
                }
            }
            q.setGremien(Set.of(gremien.toArray(new Gremium[0])));
            q.setText(form.getTxt());
            queryService.saveQuery(q);
            if (abbr != null && !abbr.isBlank()) {
                return REDIRECT_ADMIN_GREMIEN + "/" + abbr;
            } else {
                return REDIRECT_ADMIN_QUERIES;
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, QueryService.QUERY_NOT_FOUND);
        }
    }

    @GetMapping("/queries/{id}/del")
    public String deleteQuery(Model m, @RequestParam(required = false) String abbr, @PathVariable long id) {
        queryService.delQueryById(id);
        if (abbr != null && !abbr.isBlank()) {
            return REDIRECT_ADMIN_GREMIEN + "/" + abbr;
        } else {
            return REDIRECT_ADMIN_QUERIES;
        }
    }

    @PostMapping("/queries/csv")
    public String processQueryCSV(@RequestParam MultipartFile file, @RequestParam(required = false) String abbr)
            throws IOException, CsvException {
        queryService.saveQueriesFromCSV(file, abbr);
        return REDIRECT_ADMIN_QUERIES;
    }

    @PostMapping("/gremien/{abbr}/queries/new")
    public String saveNewQuery(@PathVariable String abbr) {
        return FORWARD_ADMIN_QUERIES + "/new?abbr=" + abbr;
    }

    @PostMapping(value = "/gremien/{abbr}/queries/{id}")
    public String postSaveGremiumQueryEditPage(@PathVariable String abbr, @PathVariable long id) {
        return FORWARD_ADMIN_QUERIES + "/" + id + "?abbr=" + abbr;
    }

    @GetMapping("/gremien/{abbr}/queries/{id}/delete")
    public String delGremiumQuery(@PathVariable String abbr, @PathVariable long id) {
        return FORWARD_ADMIN_QUERIES + "/" + id + "/del?abbr=" + abbr;
    }

}
