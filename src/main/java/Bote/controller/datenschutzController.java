package Bote.controller;

import org.springframework.web.bind.annotation.GetMapping;

   /**
    *   Frei zugängliche Seiten (ohne registrierung)
    *   @return
    */
public class datenschutzController {

    @GetMapping(value = "/datenschutz")
    public String datenschutz(){

        return "/datenschutz";
    }
}
