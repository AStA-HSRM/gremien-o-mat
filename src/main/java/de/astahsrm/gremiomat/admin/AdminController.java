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

import de.astahsrm.gremiomat.gremium.Gremium;
import de.astahsrm.gremiomat.gremium.GremiumService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CSVService csvService;

    @Autowired
    private GremiumService gremiumService;
    
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
    public String processUserCSV(@RequestParam("csv-datei") MultipartFile csvFile, @RequestParam("gremiumSelect") String gremiumAbbr) throws IOException, CsvException {
        csvService.generateCandidatesFromCSV(csvFile, gremiumService.getGremiumByAbbr(gremiumAbbr).get());
        return "mgmt/admin/admin";
    }

    @GetMapping("/gremien")
    public String getGremienOverview() {
        return "";
    }

    @GetMapping("/gremien/new")
    public String getNewGremiumEditPage() {
        return "";
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
    public String getGremiumEditPage(@PathVariable String abbr) {
        return "";
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

    @GetMapping("/gremien/{username}/edit")
    public String getUserEditPage(@PathVariable String username) {
        return "";
    }

    @PostMapping("/gremien/{username}/edit")
    public String postUserEditPage(@PathVariable String username) {
        return "redirect:/admin/gremien/" + username;
    }

}
