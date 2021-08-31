package de.astahsrm.gremiomat.gremium;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;


@Controller
public class GremiumController {

    @Autowired
    private GremiumService gremiumService;

    @GetMapping("gremien")
    public String getGremien(Model m) {
        m.addAttribute("all_gremiens", gremiumService.getAllGremiumsSortedByName());
        return "gremiens";
    }

    @GetMapping("gremien/{abbrv}")
    public String getGremiumInfo(@PathVariable long id, Model m) {
        Optional<Gremium> gremium = gremiumService.getGremiumById(id);
        if(gremium.isPresent()) {
            m.addAttribute("gremium", gremium);
            return "gremium_info";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
    }

    @GetMapping("gremien/add")
    public String addGremium(Model m) {
        Gremium gremium = new Gremium();
        gremium.setAbbreviation("tst");
        gremium.setDescription("test-description");
        gremium.setName("test-name");
        gremiumService.saveGremium(gremium);
        return "gremiens";
    }
}
