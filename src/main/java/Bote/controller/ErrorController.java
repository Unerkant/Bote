package Bote.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


   /**
    *   Falsch angegebene Links: error Seite ausgeben
    */

@Controller
public class ErrorController {


    @GetMapping(value = {"/{errLink}", "/messenger/{errLink}"})
    public String error(@PathVariable("errLink") String errLink, Model model){
        model.addAttribute("linkName", errLink);
        System.out.println("ERROR VON ERROR CONTROLLER: " + errLink );
        return "/error";
    }
}
