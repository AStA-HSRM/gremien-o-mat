package de.astahsrm.gremiomat.admin;

import java.io.IOException;
import java.util.Optional;

import com.opencsv.exceptions.CsvException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
            @RequestParam("gremiumSelect") String gremiumAbbr) throws IOException, CsvException, NotFoundException {
        csvService.generateCandidatesFromCSV(csvFile, gremiumAbbr);
        return "redirect:/admin/candidates";
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
        return "mgmt/admin/gremien-edit";
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
    public String getGremiumInfoPage(@PathVariable String abbr) {
        return "";
    }

    @GetMapping("/gremien/{abbr}/edit")
    public String getGremiumEditPage(@PathVariable String abbr, Model m) {
        Optional<Gremium> gremiumOptional = gremiumService.getGremiumByAbbr(abbr);
        if (gremiumOptional.isPresent()) {
            m.addAttribute("gremium", gremiumOptional.get());
            return "mgmt/admin/gremien-edit";
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
