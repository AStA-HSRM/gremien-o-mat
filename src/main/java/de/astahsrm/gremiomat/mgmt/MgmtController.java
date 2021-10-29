package de.astahsrm.gremiomat.mgmt;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import de.astahsrm.gremiomat.security.SecurityConfig;

@Controller
@RequestMapping("/mgmt")
public class MgmtController {

    @Autowired
    MgmtUserServiceImpl mgmtUserServiceImpl;

    @GetMapping
    public String redirectUser(Model m, Principal user) {
        // For testing purposes:
        if (user.getName().equalsIgnoreCase(SecurityConfig.ADMIN)) {
            return "redirect:/admin";
        }
        if (user.getName().equalsIgnoreCase(SecurityConfig.USER)) {
            return "redirect:/user";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Cannot find user: " + user.getName() + " in database!");
        }
        /* Implementation with DB:
        String userRole = mgmtUserServiceImpl.getRoleOfUserById(user.getName());
        if (userRole.equals(SecurityConfig.ADMIN)) {
            return "redirect:/admin";
        }
        if (userRole.equals(SecurityConfig.USER)) {
            return "redirect:/user";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Cannot find user: " + user.getName() + " in database!");
        }
        */
    }

}
