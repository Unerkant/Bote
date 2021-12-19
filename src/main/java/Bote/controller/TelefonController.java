package Bote.controller;

import lombok.SneakyThrows;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class TelefonController {

    public TelefonController() throws IOException, ParseException { }

    @SneakyThrows
    @GetMapping(value = "/login/telefonlogin")
    public String login(@CookieValue(value = "userid", required = false) String userId,
                        Model model){

        /**
         *   Wird benutzt nur bei Registrierung oder Einloggen
         *
         *   Daten werden geladen von laender.json für den
         *   fragments/components.html -> Länder Vorwahl Zeile: 142
         *
         *  1. Länder Daten Laden und in einen Object speichern
         */
        JSONParser jsonParser = new JSONParser();
        Object object = jsonParser.parse(new FileReader(System.getProperty("user.dir") + "/src/main/resources/static/json/laender.json"));
        JSONObject obj = (JSONObject) object;
        /* a. json-object(Komplet array) an fragments: telefonlogin.html Seite senden */
        model.addAttribute("lender", object);
        /* b. json-key(nur Buchstaben) an fragments: telefonlogin.html Seite senden */
        model.addAttribute("alphabet", ((JSONObject) object).keySet());


        /* 2. Language prüfen */
        String language = System.getProperty("user.language");

        /* 3. Language prüfen und Daten an telefonlogin.html weiter geben */
        boolean gefunden = false;
        for (Object val : obj.keySet()){
            JSONArray arr = (JSONArray)obj.get(val);
            Iterator iterator = arr.iterator();
            while (iterator.hasNext()){
                JSONObject iter = (JSONObject) iterator.next();
                String landname = String.valueOf(iter.get("stadt"));
                String landflagge = String.valueOf(iter.get("flagge"));
                String landvorwahl = String.valueOf(iter.get("vorwahl"));
                String landkurzel = String.valueOf(iter.get("language"));
                //System.out.println(" Einzelnen Wert: " +landkurzel);
                if(landkurzel.equals(language)){
                    //System.out.println("LANGUAGE: " + lang);
                    model.addAttribute("landflagge", landflagge );
                    model.addAttribute("landname", landname);
                    model.addAttribute("landvorwahl", landvorwahl );
                    gefunden = true;
                    break;
                }else {
                    landflagge = "europa";
                    landname = "Europäische Union";
                    landvorwahl = "32";
                    model.addAttribute("landflagge", landflagge );
                    model.addAttribute("landname", landname);
                    model.addAttribute("landvorwahl", landvorwahl );
                    //System.out.println("Zälen: " + landvorwahl);
                }
            }
            if (gefunden == true){
                break;
            }
        }

        return (userId == null ? "/login/telefonlogin" : "/messenger");
    }
}
