package de.astahsrm.gremiomat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class HomeController {
    
    @GetMapping("/")
    public String getHome(Model m) {
        return "home";
    }
}
