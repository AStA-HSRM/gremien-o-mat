package de.astahsrm.gremiomat.admin;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import com.opencsv.exceptions.CsvException;

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
import de.astahsrm.gremiomat.candidate.CandidateFormAdmin;
import de.astahsrm.gremiomat.candidate.CandidateService;
import de.astahsrm.gremiomat.csv.CSVService;
import de.astahsrm.gremiomat.gremium.Gremium;
import de.astahsrm.gremiomat.gremium.GremiumDto;
import de.astahsrm.gremiomat.gremium.GremiumService;
import de.astahsrm.gremiomat.photo.Photo;
import de.astahsrm.gremiomat.photo.PhotoService;
import de.astahsrm.gremiomat.query.Query;
import de.astahsrm.gremiomat.query.QueryAdminDto;
import de.astahsrm.gremiomat.query.QueryService;
import de.astahsrm.gremiomat.security.MgmtUser;
import de.astahsrm.gremiomat.security.MgmtUserService;
import de.astahsrm.gremiomat.security.SecurityConfig;
import de.astahsrm.gremiomat.username.UsernameService;
import javassist.NotFoundException;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CSVService csvService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private GremiumService gremiumService;

    @Autowired
    private QueryService queryService;

    @Autowired
    private MgmtUserService mgmtUserService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private UsernameService usernameService;

    @GetMapping
    public String getAdminPage() {
        return "admin/admin";
    }

    @GetMapping("/csv-user-upload")
    public String getUserCSVPage(Model m) {
        m.addAttribute("gremiumList", gremiumService.getAllGremiumsSortedByName());
        m.addAttribute("head2", "CSV User Upload");
        return "admin/csv-upload";
    }

    @PostMapping("/csv-user-upload")
    public String processUserCSV(@RequestParam("csv-file") MultipartFile csvFile,
            @RequestParam("gremiumSelect") String gremiumAbbr) throws IOException, CsvException, NotFoundException {
        csvService.saveCandidatesFromCSV(csvFile, gremiumAbbr);
        return "redirect:/admin/candidates";
    }

    @GetMapping("/csv-query-upload")
    public String getQueryCSVPage(Model m) {
        m.addAttribute("gremiumList", gremiumService.getAllGremiumsSortedByName());
        m.addAttribute("head2", "CSV Query Upload");
        return "admin/csv-upload";
    }

    @PostMapping("/csv-query-upload")
    public String processQueryCSV(@RequestParam("csv-file") MultipartFile csvFile,
            @RequestParam("gremiumSelect") String abbr) throws IOException, CsvException, NotFoundException {
        csvService.saveQueriesFromCSV(csvFile, abbr);
        return "redirect:/admin/gremien/" + abbr;
    }

    @GetMapping("/gremien")
    public String getGremienOverview(Model m) {
        m.addAttribute("gremienList", gremiumService.getAllGremiumsSortedByName());
        return "admin/gremien-overview";
    }

    @GetMapping("/gremien/new")
    public String getNewGremiumEditPage(Model m) {
        m.addAttribute("form", new GremiumDto());
        return "admin/gremium-edit";
    }

    @PostMapping("/gremien/new")
    public String postNewUserGremiumPage(GremiumDto form, BindingResult res, Model m) {
        if (res.hasErrors()) {
            m.addAttribute("errors", res.getAllErrors());
            return "error";
        }
        Gremium gremium = new Gremium();
        gremium.setName(form.getName());
        gremium.setAbbr(form.getAbbr());
        gremium.setDescription(form.getDescription());
        gremiumService.saveGremium(gremium);
        return "redirect:/admin/gremien";
    }

    @GetMapping("/gremien/{abbr}")
    public String getGremiumInfoPage(@PathVariable String abbr, Model m) {
        Optional<Gremium> gOpt = gremiumService.getGremiumByAbbr(abbr);
        if (gOpt.isPresent()) {
            m.addAttribute("gremium", gOpt.get());
            return "admin/gremium-info";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, GremiumService.GREMIUM_NOT_FOUND);
    }

    @GetMapping("/gremien/{abbr}/edit")
    public String getGremiumEditPage(@PathVariable String abbr, Model m) {
        Optional<Gremium> gOpt = gremiumService.getGremiumByAbbr(abbr);
        if (gOpt.isPresent()) {
            Gremium g = gOpt.get();
            GremiumDto form = new GremiumDto();
            form.setAbbr(g.getAbbr());
            form.setName(g.getName());
            form.setDescription(g.getDescription());
            m.addAttribute("form", form);
            m.addAttribute("gremium", g);
            return "admin/gremium-edit";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, GremiumService.GREMIUM_NOT_FOUND);
    }

    @PostMapping("/gremien/{abbr}/edit")
    public String postGremiumEditPage(GremiumDto form, BindingResult res, Model m) {
        if (res.hasErrors()) {
            m.addAttribute("errors", res.getAllErrors());
            return "error";
        }
        Optional<Gremium> gremiumOptional = gremiumService.getGremiumByAbbr(form.getAbbr());
        if (gremiumOptional.isPresent()) {
            Gremium gremium = gremiumOptional.get();
            gremium.setName(form.getName());
            gremium.setDescription(form.getDescription());
            return "redirect:/admin/gremien/" + gremiumService.saveGremium(gremium).getAbbr();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, GremiumService.GREMIUM_NOT_FOUND);
    }

    @GetMapping("/gremien/{abbr}/queries/{id}/edit")
    public String getGremiumQueryEditPage(@PathVariable String abbr, @PathVariable long id, Model m,
            Principal loggedInUser) {
        Optional<Query> qOpt = queryService.getQueryById(id);
        if (qOpt.isPresent()) {
            Query query = qOpt.get();
            QueryAdminDto form = new QueryAdminDto();
            form.setQueryTxt(query.getText());
            form.setGremien(query.getGremien());
            form.setId(query.getId());
            m.addAttribute("allGremien", gremiumService.getAllGremiumsSortedByName());
            m.addAttribute("form", form);
            m.addAttribute("query", query);
            m.addAttribute("role", "ADMIN");
            return "admin/query-edit";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, QueryService.QUERY_NOT_FOUND);
    }

    @PostMapping(value = "/gremien/{abbr}/queries/{id}/edit", params = "save")
    public String postSaveGremiumQueryEditPage(@PathVariable String abbr, @PathVariable long id, QueryAdminDto form,
            BindingResult res, Model m) {
        if (res.hasErrors()) {
            m.addAttribute("error", res.getAllErrors());
            return "admin/query-edit";
        } else {
            Optional<Query> qOpt = queryService.getQueryById(form.getId());
            if (qOpt.isPresent()) {
                Query q = qOpt.get();
                q.setGremien(form.getGremien());
                q.setText(form.getQueryTxt());
                q.setId(form.getId());
                queryService.saveQuery(q);
                return "redirect:/admin/gremien/" + abbr;
            }
            return "error";
        }
    }

    @PostMapping(value = "/gremien/{abbr}/queries/{id}/edit", params = "del")
    public String postDelGremiumQueryEditPage(@PathVariable String abbr, @PathVariable long id) {
        queryService.delQueryById(id);
        return "redirect:/admin/gremien/" + abbr;
    }

    @GetMapping("/users")
    public String getUserOverview(Model m) {
        m.addAttribute("allUsers", mgmtUserService.getAllUsersSortedByUsername());
        return "admin/user-overview";
    }

    @GetMapping("/users/new")
    public String getUserNew(Principal loggedInUser, Model m) {
        Candidate c = new Candidate();
        CandidateFormAdmin form = new CandidateFormAdmin();
        form.setAge(c.getAge());
        form.setBio(c.getBio());
        form.setCourse(c.getCourse());
        form.setEmail(c.getEmail());
        form.setFirstname(c.getFirstname());
        form.setGremien(c.getGremien());
        form.setLastname(c.getLastname());
        form.setSemester(c.getSemester());
        m.addAttribute("username", loggedInUser.getName());
        m.addAttribute("allGremien", gremiumService.getAllGremiumsSortedByName());
        m.addAttribute("form", form);
        return "admin/user-edit";
    }

    @PostMapping("/users/new")
    public String postNewUser(@ModelAttribute CandidateFormAdmin form, BindingResult res, Model m) {
        if (res.hasErrors()) {
            return "admin/user-edit";
        }
        Candidate c = new Candidate();
        c.setFirstname(form.getFirstname());
        c.setLastname(form.getLastname());
        c.setAge(form.getAge());
        c.setSemester(form.getSemester());
        c.setCourse(form.getCourse());
        c.setBio(form.getBio());
        c.setEmail(form.getEmail());
        c.setGremien(form.getGremien());
        if (!candidateService.getCandidateByEmail(c.getEmail()).isEmpty()) {
            MgmtUser u = new MgmtUser();
            u.setUsername(usernameService.generateUsername(c));
            u.setRole(SecurityConfig.USER);
            u.setDetails(candidateService.saveCandidate(c));
            mgmtUserService.saveUser(u);
            // TODO Send Welcome Mail after User is saved.
            return "redirect:/admin/users/";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists!");
        }
    }

    @GetMapping("/users/lock")
    public String lockUser(Model m) {
        mgmtUserService.lockAllUsers();
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{username}")
    public String getUserInfoPage(@PathVariable String username, Model m) {
        m.addAttribute("candidate", mgmtUserService.getCandidateDetailsOfUser(username));
        m.addAttribute("username", username);
        return "user/user-info";
    }

    @GetMapping("/users/{username}/edit")
    public String getUserEditPage(@PathVariable String username, Model m) {
        Candidate c = mgmtUserService.getCandidateDetailsOfUser(username);
        CandidateFormAdmin form = new CandidateFormAdmin();
        form.setAge(c.getAge());
        form.setBio(c.getBio());
        form.setCourse(c.getCourse());
        form.setEmail(c.getEmail());
        form.setFirstname(c.getFirstname());
        form.setGremien(c.getGremien());
        form.setLastname(c.getLastname());
        form.setSemester(c.getSemester());
        if (c.getPhoto() != null) {
            m.addAttribute("photoId", c.getPhoto().getId());
        }
        m.addAttribute("username", username);
        m.addAttribute("allGremien", gremiumService.getAllGremiumsSortedByName());
        m.addAttribute("form", form);
        return "admin/user-edit";
    }

    @PostMapping(value = "/users/{username}/edit", params = "del")
    public String postUserEditDel(@PathVariable String username, Model m) {
        mgmtUserService.delUserById(username);
        return "redirect:/admin/users";
    }

    @PostMapping(value = "/users/{username}/edit", params = "save")
    public String postUserEditPage(@PathVariable String username, @ModelAttribute CandidateFormAdmin form,
            BindingResult res, Model m) {
        if (res.hasErrors()) {
            return "admin/user-edit";
        }
        Candidate c = mgmtUserService.getCandidateDetailsOfUser(username);
        c.setFirstname(form.getFirstname());
        c.setLastname(form.getLastname());
        c.setAge(form.getAge());
        c.setSemester(form.getSemester());
        c.setCourse(form.getCourse());
        c.setBio(form.getBio());
        c.setEmail(form.getEmail());
        c.setGremien(form.getGremien());
        Optional<MgmtUser> uOpt = mgmtUserService.getUserById(username);
        if (uOpt.isPresent()) {
            MgmtUser u = uOpt.get();
            u.setDetails(candidateService.saveCandidate(c));
            mgmtUserService.saveUser(u);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, MgmtUserService.USER_NOT_FOUND);
        }
        return "redirect:/admin/users/" + username;
    }

    @GetMapping("/users/{username}/upload/del")
    public String getUserInfoUploadDel(@PathVariable String username, Model m) {
        Candidate c = mgmtUserService.getCandidateDetailsOfUser(username);
        Photo p = c.getPhoto();
        c.setPhoto(null);
        photoService.delPhoto(p);
        Optional<MgmtUser> uOpt = mgmtUserService.getUserById(username);
        if (uOpt.isPresent()) {
            MgmtUser u = uOpt.get();
            u.setDetails(candidateService.saveCandidate(c));
            mgmtUserService.saveUser(u);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, MgmtUserService.USER_NOT_FOUND);
        }
        return "redirect:/admin/users/" + username + "/edit";
    }

    @GetMapping("/users/{username}/upload")
    public String getUserInfoUpload(@PathVariable String username, Model m) {
        Candidate userDetails = mgmtUserService.getCandidateDetailsOfUser(username);
        if (userDetails.getPhoto() != null) {
            m.addAttribute("photoId", userDetails.getPhoto().getId());
        }
        return "user/user-info-upload";
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
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, MgmtUserService.USER_NOT_FOUND);
        }
        return "redirect:/admin/users/" + username + "/edit";
    }

}
