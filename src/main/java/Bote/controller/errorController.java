package Bote.controller;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

   /**
    *   Falsch angegebene Links: error Seite ausgeben
    */

public class errorController {

    public errorController() throws IOException, ParseException { }
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = {"/{errLink}", "/messenger/{errLink}"})
    public String error(@PathVariable("errLink") String errLink, Model model){
        model.addAttribute("linkName", errLink);
        logger.info("ERROR: " + errLink );
        return "/error";
    }
}
