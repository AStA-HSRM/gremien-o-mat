package de.astahsrm.gremiomat.gremium;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;


@Controller
@RequestMapping("/gremien")
public class GremiumController {

    @Autowired
    private GremiumService gremiumService;

    @GetMapping
    public String getGremien(Model m) {
        m.addAttribute("gremien", gremiumService.getAllGremiumsSortedByName());
        return "gremien/overview";
    }

    @GetMapping("/{abbr}")
    public String getGremiumInfo(@PathVariable String abbr, Model m) {
        Optional<Gremium> gremium = gremiumService.getGremiumByAbbr(abbr);
        if(gremium.isPresent()) {
            m.addAttribute("gremium", gremium.get());
            return "gremien/info";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
    }
    
    @GetMapping("/add")
    public String addGremium(Model m) {
        Gremium gremium = new Gremium();
        gremium.setAbbr("tst");
        gremium.setDescription("test-description");
        gremium.setName("test-name");
        gremiumService.saveGremium(gremium);
        return "redirect:/gremien";
    }
    
}
