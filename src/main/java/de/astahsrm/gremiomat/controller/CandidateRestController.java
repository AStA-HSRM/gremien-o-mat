package de.astahsrm.gremiomat.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CandidateRestController {    

    // TODO Add candidate list with answers as return value
    @GetMapping(value = "/candidates", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCandidatesWithAnswers() {
        return null;
    };

    // TODO Add candidate response with answers as return value
    @GetMapping(value = "/candidates/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCandidate(@PathVariable("id") long id) {
        return null;
    };
}
