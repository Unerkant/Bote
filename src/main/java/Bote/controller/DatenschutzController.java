package Bote.controller;

import org.springframework.web.bind.annotation.GetMapping;

   /**
    *   Frei zugängliche Seiten (ohne registrierung)
    *   @return
    */
public class DatenschutzController {

    @GetMapping(value = "/datenschutz")
    public String datenschutz(){

        return "/datenschutz";
    }
}
