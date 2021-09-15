package de.astahsrm.gremiomat.admin;

import java.io.IOException;

import com.opencsv.exceptions.CsvException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import de.astahsrm.gremiomat.candidate.CandidateService;
import de.astahsrm.gremiomat.csv.CSVService;
import de.astahsrm.gremiomat.gremium.GremiumService;
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

    @GetMapping
    public String getAdminPage() {
        return "mgmt/admin/admin";
    }

    @GetMapping("/csv-user-upload")
    public String getCSVPage(Model m) {
        m.addAttribute("gremiumList", gremiumService.getAllGremiumsSortedByName());
        return "mgmt/admin/csv-user-upload";
    }

    @PostMapping("/csv-user-upload")
    public String processUserCSV(@RequestParam("csv-file") MultipartFile csvFile,
            @RequestParam("gremiumSelect") String gremiumAbbr) {
        try {
            csvService.generateCandidatesFromCSV(csvFile, gremiumAbbr);
            return "redirect:/admin/candidates";
        } catch (IOException | CsvException e) {
            return "error/400";
        } catch (NotFoundException e) {
            return "error/404";
        }
    }

    // TODO temporary site for debugging
    @GetMapping("/candidates")
    public String getUsers(Model m) {
        m.addAttribute("candidateList", candidateService.getAllCandidatesSortedByName());
        return "candidate/candidates";
    }

    @GetMapping("/gremien")
    public String getGremienOverview(Model m) {
        m.addAttribute("gremienList", gremiumService.getAllGremiumsSortedByName());
        return "mgmt/admin/gremien-overview";
    }

    @GetMapping("/gremien/new")
    public String getNewGremiumEditPage() {
        return "mgmt/admin/gremien-edit";
    }

    @PostMapping("/gremien/new")
    public String postNewUserGremiumPage() {
        return "redirect:/admin/gremien";
    }

    @GetMapping("/gremien/{abbr}")
    public String getGremiumInfoPage(@PathVariable String abbr) {
        return "";
    }

    @GetMapping("/gremien/{abbr}/edit")
    public String getGremiumEditPage(@PathVariable String abbr, Model m) {
        m.addAttribute("gremium", gremiumService.getGremiumByAbbr(abbr).get());
        return "mgmt/admin/gremien-edit";
    }

    @PostMapping("/gremien/{abbr}/edit")
    public String postGremiumEditPage(@PathVariable String abbr) {
        return "";
    }

    @GetMapping("/gremien/{abbr}/{queryIndex}/edit")
    public String getGremiumQueryEditPage(@PathVariable String abbr, @PathVariable int queryIndex) {
        return "";
    }

    @PostMapping("/gremien/{abbr}/{queryIndex}/edit")
    public String postGremiumQueryEditPage(@PathVariable String abbr, @PathVariable int queryIndex) {
        return "";
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
