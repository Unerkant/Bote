package Bote.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class KontakteController {
    private List    kontakteFreunde;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/kontakte")
    public String contakte(@CookieValue(value = "userid", required = false) String kontaktcookie,
                           HttpServletRequest request, Model model){


        logger.info("Kontakte" + kontaktcookie);
        return (kontaktcookie == null ? "/login/maillogin" : "/kontakte");
    }
}
