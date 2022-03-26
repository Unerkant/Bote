package Bote.controller;

import Bote.configuration.GlobalConfig;
import Bote.model.Freunde;
import Bote.model.User;
import Bote.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
    private MessageService messageService;
    @Autowired
    private PhoneService phoneService;
    @Autowired
    private SessionService sessionService;
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
        *   try: verhindert den Totalen ERROR: Whitelabel Error Page
        *   Fehler: vermutlich meineDaten- sind leer
        *   NumberFormatException: Cannot parse null string
        *
        */
        try {
            meineDaten = userService.findeUserToken(Long.valueOf(meineId));
            model.addAttribute("meinedaten", meineDaten);
            model.addAttribute("myToken", meineId);
        }catch (Exception e){
            logger.info("Exception Fehler: " + e);
        }

        logger.info("SettingController/GetMapping: " + meineId +"/"+ meineDaten);
        return (meineId == null ? "/login/maillogin" : "/setting");
    }


   /**
    *   Bild Upload
    */
    private Integer bild;
    @PostMapping(value = "bildupload")
    @ResponseBody
    public Map<String, Object> bildUpload(@CookieValue(value = "userid", required = false) String uploadCookie,
                                          @RequestParam(value = "imageurl", defaultValue = "") String imageBase) {

        Map<String, Object> res = new HashMap<String, Object>();
        //File imageFile = new File(String.valueOf(this.getClass().getResourceAsStream("/static/profilbild/"+uploadCookie+".png")));
        //File imageFile = new File("/static/profilbild/"+uploadCookie+".png");
        File imageFile = new File("/Users/paulrichter/ideaProject/Bote/src/main/resources/static/profilbild/"+uploadCookie+".png");
        try {
            byte[] decodedBytes = DatatypeConverter.parseBase64Binary(imageBase.replaceAll("data:image/.+;base64,", ""));
            BufferedImage buff = ImageIO.read(new ByteArrayInputStream(decodedBytes));
            ImageIO.write(buff, "png", imageFile);
            buff.flush();
            res.put("ret", 200);
            // Bild Name in Datenbank Tabelle: user aktualisieren
            bild = userService.bildUpdate(uploadCookie, uploadCookie);
            //logger.info("TRY: " + buff);
        }catch (Exception e){
            res.put("ret", -1);
            res.put("msg", "Verarbeitung aufgrund eines Bildverarbeitungsfehlers nicht möglich.");
            return res;
        }

        logger.info("Bild Upload Image: " + bild);
        return res;
    }


   /**
    *   Setting: Steuerung von einzelnen bereiche
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
        return (profilcookie == null ? "/login/maillogin" : "/setting :: #FRAGMENTANZEIGEN");
        //return "setting ::" + showprofil;
    }


   /**
    *   Profil Save
    *
    *   in settingcomponents.html
    */

    private Integer     vornameUpdate;
    private Integer     nameUpdate;
    private Integer     mailUpdate;
    private Integer     telefonUpdate;
    private String      output;
    @PostMapping(value = "/profilsave")
    @ResponseBody
    public String profilSave(@CookieValue(value = "userid", required = false) String profilcookie,
                             @RequestParam(value = "tokensave", required = false) String tokenSave,
                             @RequestParam(value = "namesave", required = false) String nameSave,
                             @RequestParam(value = "valuesave", required = false) String valueSave )
    {
        switch (nameSave){
            case"vorname":      vornameUpdate = settingService.vornameUpdate(valueSave, tokenSave);
                                if (vornameUpdate.equals(1)){
                                    output = "vorname";
                                }else{
                                    output = "profilerror";
                                }
                                break;
            case"name":         nameUpdate = settingService.nameUpdate(valueSave, tokenSave);
                                if (nameUpdate.equals(1)){
                                    output = "name";
                                }else{
                                    output = "profilerror";
                                }
                                break;
            case"mail":         mailUpdate = settingService.mailUpdate(valueSave, tokenSave);
                                if (mailUpdate.equals(1)){
                                    output = "mail";
                                }else {
                                    output = "profilerror";
                                }
                                break;
            case"telefon":      telefonUpdate = settingService.telefonUpdate(valueSave, tokenSave);
                                if (telefonUpdate.equals(1)){
                                    output = "telefon";
                                }else {
                                    output = "profilerror";
                                }
                                break;
            default:            output = "profilerror";
                                break;
        }

        logger.info("POSTMAPPING/ Profil Save: " +tokenSave +"/"+nameSave +"/"+valueSave);
        //return an javascript profil.js function profilSave() Zeile:120
        return (profilcookie == null ? "/login/maillogin" : output);
    }

   /**
    * Code generieren und zurück senden.
    * bei änderungen das E-Mail, Telefon oder Accound Löschen
    * ein e-mail mit freischaltcode an die ALTE E-Mail-adresse senden
    * die freischaltcode wird zusäzlich als return an die profil.js
    * fonction codeHolen(.. auch zugesendet(zum vergleichen)
    *
    * Zugesendete Daten(von profil.js/ codeHolen()...)
    * codetoken = meinen Token
    * codename  = was soll versendet, mail oder telefon
    * codevalue = an wem soll versendet: mail-adresse oder telefonnummer(SMS)
    * codevalue: ZURZEIT NICHT GENUTZT, WIRD VERSENDET AN ALTE MAIL-ADRESSE ODER TELEFONNUMMER
    */
    private User    myData;
    private String  alteMail;
    private String  alteTelefon;
    private int     code;
    private String  mail;
    private String  telefon;
    @PostMapping(value = "/codeHolen")
    @ResponseBody
    public String codeHolen(@RequestParam(value = "codetoken", required = false) String codetoken,
                            @RequestParam(value = "codename", required = false) String codename,
                            @RequestParam(value = "codevalue", required = false) String codevalue,
                            Model model)
    {

        myData      = userService.findeUserToken(Long.valueOf(codetoken));
        alteMail    = myData.getEmail();
        alteTelefon = myData.getTelefon();

        code = GlobalConfig.aktivierungCode();
        logger.info("codeHolen: "+ codename+ "/" +codevalue+ "/" + alteMail +"/"+ alteTelefon + "/" + code);

        /* mail mit code an alte E-Mail-Adresse vesenden */
        if (alteMail != null && !alteMail.isBlank()){
            /*mail = GlobalConfig.mailSenden(alteMail, code);
            if (mail.equals("nomail")){
                return "nomail";
            }*/
        }
        /* sms mit code an die alte Telefonnummer versenden */
        if (alteTelefon != null && !alteTelefon.isBlank()){
            /*telefon = GlobalConfig.smsSenden(alteTelefon, code);
            if (telefon.equals("nosms")){
                return "nosms";
            }*/
        }
        /*
         *  code an prfil.js/codeHolen()... senden
         */
        return String.valueOf(code);


    }

   /**
    *   Account Abmelden
    *   werden nur cookie gelöscht(in profil.js) und hier
    *   die Tabelle Session in Spalte 'letztenoutlog' datum aktualisiert
    */
    private Integer outlog;
    private String  datum;
    @PostMapping(value = "/accountabmelden")
    @ResponseBody
    public String accountAbmelden(@RequestParam(value = "abmeldentoken") String token){
        datum   = GlobalConfig.deDatum();
        outlog  = sessionService.letztenoutlogUpdate(datum, token);
        logger.info("Account Abmelden: " +datum +"/"+ outlog);
        return "abgemeldet";
    }


   /**
    *   Account Loschen
    */
    private User            userDaten;
    private String          userPseudonym;
    private String          userName;
    private String          userVorname;
    private List<String>    alleMeineMessageToken;
    private String          phoneGeloscht;
    private String          sessionGeloscht;
    private String          freundeGeloscht;
    private String          messagesGeloscht;
    private String          userGeloscht;
    private String          cookieGeloscht;
    @PostMapping(value = "/accountloschen")
    //@ResponseBody
    public String accountLoschen(@CookieValue(value = "userid", required = false) String cookie,
                                 @RequestParam(value = "token", required = false) String token,
                                 HttpServletResponse response, Model model)
    {
        // Meine Daten ermiteln
        userDaten       = userService.findeUserToken(Long.valueOf(token));
        userPseudonym   = userDaten.getPseudonym();
        userName        = userDaten.getName();
        userVorname     = userDaten.getVorname();
        // finden in H2 das gleiche messageToken wie von meiner Chat-Freund
        alleMeineMessageToken = freundeService.freundeSuchen(String.valueOf(cookie))
                .stream()
                .map(Freunde::getMessagetoken).collect(Collectors.toList());

    /* 1 */    /* 100% Phone Tabbelle leeren, return int: bei leer 0 */
        phoneGeloscht = phoneService.allUserTelfonatLoschen(token);

    /* 2 */     /* 100% Session Tabelle leeren, return int: bei leer 0 */
        sessionGeloscht = sessionService.allUserSessionLoschen(token);

    /* 3 */     /* 100% Freunde Tabelle leeren, return int: bei leer 0  */
        freundeGeloscht = freundeService.allFreundeLoschen(alleMeineMessageToken);

    /* 4 */     /* 100% Messages Tabelle leeren, return int: bei leer 0 */
        messagesGeloscht = messageService.allMessagesLoschen(alleMeineMessageToken);

    /* 5 */     /* 100% User Tabelle leeren, return int: bei leer 0 */
        userGeloscht = userService.userLoschen(Long.valueOf(token));

    /* 6 */     /* 100% cookie löschen */
        cookieGeloscht = GlobalConfig.deleteCookie(response);

        model.addAttribute("frName", "accountloschen");
        model.addAttribute("userpseudonym", userPseudonym);
        model.addAttribute("username", userName);
        model.addAttribute("uservorname", userVorname);
        model.addAttribute("usercount", userGeloscht);
        model.addAttribute("messagecount", messagesGeloscht);
        model.addAttribute("freundecount", freundeGeloscht);
        model.addAttribute("sessioncount", sessionGeloscht);
        model.addAttribute("phonecount", phoneGeloscht);
        logger.info("Account Loschen: " + alleMeineMessageToken +"/"+ phoneGeloscht +"/"+ sessionGeloscht + "/"
                + freundeGeloscht +"/"+ messagesGeloscht +"/"+ userGeloscht +"/"+ cookieGeloscht);
        return "/setting :: #ACCOUNTLOSCHENFRAGMENT";
    }
}
