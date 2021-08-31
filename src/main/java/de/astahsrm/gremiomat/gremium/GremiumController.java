package de.astahsrm.gremiomat.gremium;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class GremiumController {
    @GetMapping(value="{gremium}")
    public String getMethodName(@PathVariable("gremium") String gremium) {
        // TODO something something get Gremium by id...
        return "gremium_info";
    };
}