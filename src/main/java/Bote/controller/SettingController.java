package Bote.controller;

import Bote.model.User;
import Bote.service.FreundeService;
import Bote.service.SettingService;
import Bote.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;


@Controller
public class SettingController {

    private User            meineDaten;
    private String          meinenToken;
    private User            meinerDaten;
    private String          fragmentName;
    private String          fragmentTitel;
    private String          volleFragmentTitel;

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private FreundeService freundeService;
    @Autowired
    private UserService userService;
    @Autowired
    private SettingService settingService;

   /**
    *   Setting HTML Starten
    *   Linke Seite: Ausgabe von Bild & E-Mail-Adresse
    */

    @GetMapping(value = "/setting")
    public String setting(@CookieValue(value = "userid", required = false) String meineId,
                          Model model)
    {
       /*
        *   Daten für setting.html/profil(Linke Seite)
        */
        meineDaten = userService.findeUserToken(Long.valueOf(meineId));
        model.addAttribute("meinedaten", meineDaten);
        model.addAttribute("myToken", meineId);

        logger.info("SettingController/GetMapping: " + meineId +"/"+ meineDaten);
        return (meineDaten == null ? "/login/maillogin" : "/setting");
    }


   /**
    *   Setting Profil
    *   attributeName: fragmentName (erkennungs variable)
    *   in settingcomponents.html (wird bei jedem Fragment geprüft)
    */
    @PostMapping(value = "/einstellung")
    public String profilFragment(@CookieValue(value = "userid", required = false) String profilcookie,
                                 Model model,
                                 HttpServletRequest request){

        meinenToken     = request.getParameter("myToken");
        fragmentName    = request.getParameter("fragmentName");
        fragmentTitel   = request.getParameter("fragmentTitel");

        meinerDaten     = userService.findeUserToken(Long.valueOf(profilcookie));

        model.addAttribute("fragmentName", fragmentName);
        model.addAttribute("meinerDaten", meinerDaten);

        switch (fragmentTitel){
            case "profil":      volleFragmentTitel = "Profil bearbeiten"; break;
            case "allgemein":   volleFragmentTitel = "Allgemeine Einstellungen"; break;
            case "darstellung": volleFragmentTitel = "Erscheinungsbild"; break;
            case "privatsicher":volleFragmentTitel = "Privatsphäre & Sicherheit"; break;
            case "mitteilung":  volleFragmentTitel = "Mitteilungen & Töne"; break;
            case "sprache":     volleFragmentTitel = "Sprache"; break;
            case "sticker":     volleFragmentTitel = "Sticker"; break;
            case "botefaq":     volleFragmentTitel = "Bote FAQ"; break;
            case "support":     volleFragmentTitel = "Frage & Support"; break;
            default:            volleFragmentTitel = "Allgemein";
        }
        /* den vollen Titel text in Fragment Head ausgeben settingcomponents.html Zeile: 26  */
        model.addAttribute("fragmentTitel", volleFragmentTitel);

        logger.info("POSTMAPPING/EINSTELLUNG: / " + fragmentTitel +"/"+ volleFragmentTitel );
        return (meinerDaten == null ? "/login/maillogin" : "/setting :: #FRAGMENTANZEIGEN");
        //return "setting ::" + showprofil;
    }


    /**
     *   Profil Save
     *
     *   in settingcomponents.html
     */
    private Integer     vornameUpdate;
    private String      output;
    @PostMapping(value = "/profilsave")
    @ResponseBody
    public String profilSave(@CookieValue(value = "userid", required = false) String profilcookie,
                             @RequestParam(value = "tokensave", required = false) String tokenSave,
                             @RequestParam(value = "namesave", required = false) String nameSave,
                             @RequestParam(value = "valuesave", required = false) String valueSave,
                             Model model ){

        switch (nameSave){
            case"bild": break;
            case"vorname":  vornameUpdate = settingService.vornameUpdate(valueSave, tokenSave);
                            if (vornameUpdate == 1){
                                output = "vorname";
                            }else{
                                output = "nichtgespeichert";
                            }
                            break;
            case"name":     vornameUpdate = settingService.nameUpdate(valueSave, tokenSave);
                            if (vornameUpdate == 0){
                                output = "name";
                            }else{
                                output = "nichtgespeichert";
                            }
                            break;
            case"mail": break;
            case"telefon": break;
            case"abmelden": break;
            case"loschen": break;
            default: break;
        }

        logger.info("POSTMAPPING/ Profil Save: " + vornameUpdate +"/"+tokenSave +"/"+nameSave +"/"+valueSave);
        //return String.valueOf(new ResponseEntity<String>("output:" + profilcookie, HttpStatus.OK));
        return (profilcookie == null ? "/login/maillogin" : output);
    }
}
