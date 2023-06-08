package Bote.controller;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

   /**
    *   Falsch angegebene Links: error Seite ausgeben
    */

@Controller
public class ErrorController {

    public ErrorController() throws IOException, ParseException { }
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = {"/{errLink}", "/messenger/{errLink}"})
    public String error(@PathVariable("errLink") String errLink, Model model){
        model.addAttribute("linkName", errLink);
        System.out.println("ERROR VON ERROR CONTROLLER: " + errLink );
        return "/error";
    }
}
