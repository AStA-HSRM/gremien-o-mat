package de.astahsrm.gremiomat.error;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import de.astahsrm.gremiomat.gremium.GremiumService;

@Controller
public class GremienErrorController implements ErrorController {

    @Autowired
    private GremiumService gremiumService;

    @RequestMapping("/error")
    public String handleError(Model m, HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        m.addAllAttributes(gremiumService.getGremienNavMap());
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            switch (HttpStatus.valueOf(statusCode)) {
            case NOT_FOUND:
                return "error/404";
            case INTERNAL_SERVER_ERROR:
                return "error/500";
            case BAD_REQUEST:
                return "error/400";
            default:
                return "error";
            }
        }
        return "error";
    }
}
