package Bote.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


/**
 * bearbeitet am 5.06.2023
 */

@Controller
public class KontakteController {

    private List    kontakteFreunde;

    @GetMapping(value = "/kontakte")
    public String contakte(@CookieValue(value = "userid", required = false) String kontaktcookie,
                           HttpServletRequest request, Model model){

        model.addAttribute("kontaktRequestUri", request.getRequestURI());

        System.out.println("Kontakte" + kontaktcookie);
        return (kontaktcookie == null ? "/login/maillogin" : "/kontakte");
    }
}
