package Bote.controller;

import Bote.model.Freunde;
import Bote.model.User;
import Bote.service.FreundeService;
import Bote.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class SettingController {

    private List<Freunde> meinefreunde;
    private User            meineDaten;
    private String          meinenToken;
    private User            profilDaten;
    private String          fragmentName;
    private String          fragmentTitel;
    private String          volleFragmentTitel;

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private FreundeService freundeService;
    @Autowired
    private UserService userService;

   /**
    *   Setting HTML Starten
    *   Linke Seite: Ausgabe von Bild & E-Mail-Adresse
    */

    @GetMapping(value = "/setting")
    public String setting(@CookieValue(value = "userid", required = false) String meineId, Model model,
                        HttpServletRequest request, HttpServletResponse response)
    {
       /*
        *   Daten für setting.html/profil(Linke Seite)
        */
        meineDaten = userService.findeUserToken(Long.valueOf(meineId));
        model.addAttribute("meinedaten", meineDaten);
        model.addAttribute("myToken", meineId);

        logger.info("SettingController/GetMapping: " + meineId +"/"+ meineDaten);
        return (meineId == null ? "/login/maillogin" : "/setting");
    }


   /**
    *   Setting Profil
    *   attributeName: switchFragmentName (erkennungs variable)
    *   in settingcomponents.html  SWITCH function
    */
    @PostMapping(value = "/einstellung")
    public String profilFragment(@CookieValue(value = "userid", required = false) String profilcookie,
                            Model model, HttpServletRequest request){

        meinenToken     = request.getParameter("myToken");
        fragmentName    = request.getParameter("fragmentName");
        fragmentTitel   = request.getParameter("fragmentTitel");

        profilDaten     = userService.findeUserToken(Long.valueOf(profilcookie));


        model.addAttribute("fragmentName", fragmentName);
        model.addAttribute("profilDaten", profilDaten);

        switch (fragmentTitel){
            case "profil": volleFragmentTitel = "Profil bearbeiten"; break;
            case "allgemein": volleFragmentTitel = "Allgemeine Einstellungen"; break;
            case "darstellung": volleFragmentTitel = "Mitteilung & Töne"; break;
            case "privatsicher": volleFragmentTitel = "Privatsph&#228;re & Sicherheit"; break;
            case "mitteilung": volleFragmentTitel = "Mitteilungen & T&#246;ne"; break;
            case "sprache": volleFragmentTitel = "Sprache"; break;
            case "sticker": volleFragmentTitel = "Sticker"; break;
            case "botefaq": volleFragmentTitel = "Bote FAQ"; break;
            case "support": volleFragmentTitel = "Frage & Support"; break;
            default: volleFragmentTitel = "Allgemein";
        }
        model.addAttribute("fragmentTitel", volleFragmentTitel);

        logger.info("POSTMAPPING/EINSTELLUNG: / " + fragmentTitel );
        return (profilcookie == null ? "/login/maillogin" : "/setting :: #FRAGMENTANZEIGEN");
        //return "setting ::" + showprofil;
    }


    /**
     *   Setting Allgemein
     *   attributeName: switchFragmentName (erkennungs variable)
     *   in settingcomponents.html  SWITCH function
     */
/*    @PostMapping(value = "/allgemein")
    public String allgemeinFragment(@CookieValue(value = "userid", required = false) String allgemeincookie,
                                 Model model, HttpServletRequest request){

        frFragmentName = request.getParameter("fragmentName");
        model.addAttribute("ifFragmentName", frFragmentName);

        logger.info("POSTMAPPING/ALLGEMEIN: /#SHOW_" + frFragmentName);
        return (allgemeincookie == null ? "/login/maillogin" : "/setting :: #SHOW_" + frFragmentName);
        //return "setting ::" + showprofil;
    }*/
}
