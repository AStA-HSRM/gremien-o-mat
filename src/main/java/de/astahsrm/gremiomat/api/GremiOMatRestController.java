package de.astahsrm.gremiomat.api;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.astahsrm.gremiomat.candidate.Candidate;
import de.astahsrm.gremiomat.gremium.GremiumService;
import javassist.NotFoundException;

@RestController
@RequestMapping("/api/v1")
public class GremiOMatRestController {

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

    @GetMapping(value = "/gremium/{abbr}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Candidate> getGremiumCandidates(@PathVariable("abbr") String abbr) throws NotFoundException {
        return gremiumService.getGremiumCandidatesByGremiumAbbr(abbr);
    }
}
