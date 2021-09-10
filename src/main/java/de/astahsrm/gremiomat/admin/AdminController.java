package de.astahsrm.gremiomat.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @GetMapping
    public String getAdminPage() {
        return "mgmt/admin/admin-overview";
    }

    @GetMapping("/csv-user-upload")
    public String getCSVPage() {
        return "mgmt/admin/csv-user-upload";
    }
    
    @PostMapping("/csv-user-upload")
    public String processUserCSV() {
        return "";
    }


}
