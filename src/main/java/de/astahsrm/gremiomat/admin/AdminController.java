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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import de.astahsrm.gremiomat.candidate.CandidateService;
import de.astahsrm.gremiomat.csv.CSVService;
import de.astahsrm.gremiomat.gremium.Gremium;
import de.astahsrm.gremiomat.gremium.GremiumService;
import de.astahsrm.gremiomat.query.EditQueryForm;
import de.astahsrm.gremiomat.query.Query;
import de.astahsrm.gremiomat.query.QueryService;
import javassist.NotFoundException;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CSVService csvService;

    @Autowired
    private GremiumService gremiumService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private QueryService queryService;

    @GetMapping
    public String getAdminPage() {
        return "mgmt/admin/admin";
    }

    @GetMapping("/csv-user-upload")
    public String getUserCSVPage(Model m) {
        m.addAttribute("gremiumList", gremiumService.getAllGremiumsSortedByName());
        m.addAttribute("head2", "CSV User Upload");
        return "mgmt/admin/csv-upload";
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
        return "mgmt/admin/csv-upload";
    }

    @PostMapping("/csv-query-upload")
    public String processQueryCSV(@RequestParam("csv-file") MultipartFile csvFile,
            @RequestParam("gremiumSelect") String abbr) throws IOException, CsvException, NotFoundException {
        csvService.saveQueriesFromCSV(csvFile, abbr);
        return "redirect:/admin/gremien/" + abbr;
    }

    @GetMapping("/candidates")
    public String getUsers(Model m) {
        m.addAttribute("candidateList", candidateService.getAllCandidatesSortedByName());
        return "mgmt/admin/user-overview";
    }

    @GetMapping("/gremien")
    public String getGremienOverview(Model m) {
        m.addAttribute("gremienList", gremiumService.getAllGremiumsSortedByName());
        return "mgmt/admin/gremien-overview";
    }

    @GetMapping("/gremien/new")
    public String getNewGremiumEditPage() {
        return "mgmt/admin/gremium-edit";
    }

    @PostMapping("/gremien/new")
    public String postNewUserGremiumPage(@RequestParam("gremium-name") String name,
            @RequestParam("gremium-abbr") String abbr, @RequestParam("gremium-desc") String desc) {
        Gremium gremium = new Gremium();
        gremium.setName(name);
        gremium.setAbbr(abbr);
        gremium.setDescription(desc);
        gremiumService.saveGremium(gremium);
        return "redirect:/admin/gremien";
    }

    @GetMapping("/gremien/{abbr}")
    public String getGremiumInfoPage(@PathVariable String abbr, Model m) {
        Optional<Gremium> gremiumOptional = gremiumService.getGremiumByAbbr(abbr);
        if (gremiumOptional.isPresent()) {
            m.addAttribute("gremium", gremiumOptional.get());
            return "mgmt/gremium-overview";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, GremiumService.GREMIUM_NOT_FOUND);
    }

    @GetMapping("/gremien/{abbr}/edit")
    public String getGremiumEditPage(@PathVariable String abbr, Model m) {
        Optional<Gremium> gremiumOptional = gremiumService.getGremiumByAbbr(abbr);
        if (gremiumOptional.isPresent()) {
            m.addAttribute("gremium", gremiumOptional.get());
            return "mgmt/admin/gremium-edit";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, GremiumService.GREMIUM_NOT_FOUND);
    }

    @PostMapping("/gremien/{abbr}/edit")
    public String postGremiumEditPage(@RequestParam("gremium-name") String name,
            @RequestParam("gremium-abbr") String abbr, @RequestParam("gremium-desc") String desc) {
        Optional<Gremium> gremiumOptional = gremiumService.getGremiumByAbbr(abbr);
        if (gremiumOptional.isPresent()) {
            Gremium gremium = gremiumOptional.get();
            gremium.setName(name);
            gremium.setDescription(desc);
            gremiumService.saveGremium(gremium);
            return "redirect:/admin/gremien";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, GremiumService.GREMIUM_NOT_FOUND);
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
                form.setQueryTxt(query.getText());
                form.setGremien(query.getGremien());
                form.setId(query.getId());
                m.addAttribute("allGremien", gremiumService.getAllGremiumsSortedByName());
                m.addAttribute("form", form);
                m.addAttribute("query", query);
                m.addAttribute("role", "ADMIN");
                return "mgmt/user-query-edit";
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, QueryService.QUERY_NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, GremiumService.GREMIUM_NOT_FOUND);
    }

    @PostMapping(value = "/gremien/{abbr}/{queryIndex}/edit", params = "save")
    public String postSaveGremiumQueryEditPage(@PathVariable String abbr, @PathVariable int queryIndex, EditQueryForm form, BindingResult res, Model m) {
        if(res.hasErrors()) {
            m.addAttribute("error", res.getAllErrors());
            return "mgmt/user-query-edit";
        } else {
            Optional<Query> qOpt = queryService.getQueryById(form.getId());
            if(qOpt.isPresent()) {
                Query q = qOpt.get();
                q.setGremien(form.getGremien());
                q.setText(form.getQueryTxt());
                q.setId(form.getId());
                queryService.saveQuery(q);
                return "redirect:/admin/gremien";
            }
            return "error";
        }
    }

    @PostMapping(value = "/gremien/{abbr}/{queryIndex}/edit", params = "del")
    public String postDelGremiumQueryEditPage(@PathVariable String abbr, @PathVariable int queryIndex) {
        queryService.delQueryByIndexAndGremium(queryIndex, abbr);
        return "redirect:/admin/gremien/" + abbr;
    }

    @GetMapping("/users")
    public String getUserOverview() {
        return "";
    }

    @GetMapping("/users/new")
    public String getNewUserEditPage() {
        return "";
    }

    @PostMapping("/users/new")
    public String postNewUserEditPage() {
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{username}")
    public String getUserInfoPage(@PathVariable String username) {
        return "";
    }

    @GetMapping("/users/{username}/edit")
    public String getUserEditPage(@PathVariable String username) {
        return "";
    }

    @PostMapping("/users/{username}/edit")
    public String postUserEditPage(@PathVariable String username) {
        return "redirect:/admin/users/" + username;
    }

}
