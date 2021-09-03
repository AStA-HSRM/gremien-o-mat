package de.astahsrm.gremiomat.api;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.astahsrm.gremiomat.gremium.GremiumService;
import de.astahsrm.gremiomat.gremium.Gremium;

@RestController
@RequestMapping("/api/v1")
public class GremiOMatRestController {

    // TODO how do you do this
    @Autowired
    private GremiumService gremiumService;

    @GetMapping(value = "/candidates", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCandidatesWithAnswers() {
        // TODO Add candidate list with answers as return value
        return null;
    }

    @GetMapping(value = "/candidates/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCandidate(@PathVariable("id") long id) {
        // TODO Add candidate response with answers as return value
        return null;
    };

    @GetMapping(value = "/gremium/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Gremium> getGremiumCandidates(@PathVariable("id") long id) {
        // TODO This is wrong, needs getCandidatesByGremium function
        return gremiumService.getGremiumById(id);
    }
}
