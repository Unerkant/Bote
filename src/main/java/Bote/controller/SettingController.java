package Bote.controller;

import Bote.model.Freunde;
import Bote.model.Usern;
import Bote.service.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
public class SettingController {

    @Autowired
    private MethodenService methodenService;
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
    @Autowired
    private CountEntryService entryService;


    /**
     *   Setting HTML Starten
     *   Linke Seite: Ausgabe von Bild & E-Mail-Adresse
     */
    private Usern    meineDaten;
    @GetMapping(value = "/setting")
    public String setting(@CookieValue(value = "userid", required = false) String meineId,
                          HttpServletRequest request,
                          Model model)
    {
       /**
        *   Daten für setting.html/profil(Linke Seite)
        *   try: verhindert den Totalen ERROR: Whitelabel Error Page
        *   Fehler: vermutlich meineDaten- sind leer
        *   NumberFormatException: Cannot parse null string
        *
        */
        meineDaten = userService.meineDatenHolen(meineId);
        model.addAttribute("meinedaten", meineDaten);
        model.addAttribute("myToken", meineId);
        model.addAttribute("settingRequestUri", request.getRequestURI());

        //logger.info("SettingController/GetMapping: " + meineId +"/"+ meineDaten);
        return (meineId == null ? "/login/maillogin" : "/setting");
    }



    /**
     *   Profil Bild Upload
     *   Bild-Name wird das Gleiche gegeben wie user token
     */
    private Integer bildBeiUsern;
    private Integer bildBeiFreunde;
    @PostMapping(value = "bildupload")
    @ResponseBody
    public Map<String, Object> bildUpload(@CookieValue(value = "userid", required = false) String uploadCookie,
                                          @RequestParam(value = "imageurl", defaultValue = "") String imageBase)
    {

        Map<String, Object> res = new HashMap<String, Object>();
        File imageFile = new File("./profilbild/"+uploadCookie+".png");
        imageFile.mkdirs();
        try {
            byte[] decodedBytes = DatatypeConverter.parseBase64Binary(imageBase.replaceAll("data:image/.+;base64,", ""));
            BufferedImage buff = ImageIO.read(new ByteArrayInputStream(decodedBytes));
            ImageIO.write(buff, "png", imageFile);
            buff.flush();
            // Bild Name in Datenbank Tabelle: user & freunde aktualisieren
            bildBeiUsern    = settingService.usernBildUpdate(uploadCookie, uploadCookie);
            bildBeiFreunde  = settingService.freundeBildUpdate(uploadCookie, uploadCookie);
            res.put("ret", 200);
            //logger.info("TRY: " + decodedBytes);
        }catch (Exception e){
            res.put("ret", -1);
            //res.put("msg", "Verarbeitung aufgrund eines Bildverarbeitungsfehlers nicht möglich.");
            return res;
        }

        //System.out.println("Bild Upload Image: " + bildBeiUsern +"/"+ bildBeiFreunde);
        return res;
    }



    /**
     *   Profil Bild Löschen
     *   ACHTUNG: nur in Datenbank der Name von Bild
     *   nameBild - ist das gleiche wie user token
     */
    private Integer usernBildGeloscht;
    private Integer freundeBildGeloscht;
    private Integer bildGeloscht;
    @PostMapping(value = "/profilbildloschen")
    @ResponseBody
    public String profilbildLoschen(@RequestParam(value = "bildname", required = true) String nameBild) throws Exception
    {
        usernBildGeloscht = settingService.usernBildUpdate("", nameBild);
        freundeBildGeloscht = settingService.freundeBildUpdate("", nameBild);
        bildGeloscht = usernBildGeloscht + freundeBildGeloscht;

        //System.out.println("Profil Bild Loöschen: " + bildGeloscht);

        return String.valueOf(bildGeloscht);
    }



    /**
     *   Setting: Steuerung von einzelnen bereiche
     *   attributeName: fragmentName (erkennungs variable)
     *   in profil.html (wird bei jedem Fragment geprüft)
     */
    private String          meinenToken;
    private Usern           meinerDaten;
    private String          fragmentName;
    private String          fragmentTitel;
    private String          itemId;
    private String          volleFragmentTitel;
    @PostMapping(value = "/einstellung")
    public String profilFragment(@CookieValue(value = "userid", required = false) String profilcookie,
                                 Model model,
                                 HttpServletRequest request,
                                 HttpSession session){

        meinenToken     = request.getParameter("myToken");
        fragmentName    = request.getParameter("fragmentName");
        fragmentTitel   = request.getParameter("fragmentTitel");
        itemId          = request.getParameter("itemId");

        String links = "/fragments/setting/"+itemId;

        meinerDaten     = userService.meineDatenHolen(profilcookie);

        model.addAttribute("fragmentName", fragmentName);
        model.addAttribute("meinerDaten", meinerDaten);
        model.addAttribute("fragmentHtml", itemId);
        //session.setAttribute("fragmentHtml", itemId);


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
        /* den vollen Titel text in Fragment Head ausgeben profil.html Zeile: 26  */
        model.addAttribute("fragmentTitel", volleFragmentTitel);

        //System.out.println("POSTMAPPING/EINSTELLUNG: / " + fragmentName +"/"+ itemId  );

        return (profilcookie == null ? "/login/maillogin" : "/setting :: #FRAGMENTANZEIGEN");
        //return "setting ::" + showprofil;
    }



    /**
     *   Profil Save
     *
     *   in profil.html
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

        //System.out.println("POSTMAPPING/ Profil Save: " +tokenSave +"/"+nameSave +"/"+valueSave);

        //return an javascript profil.js function profilSave() Zeile:120
        return (profilcookie == null ? "/login/maillogin" : output);
    }



    /**
     * Code generieren und zurücksenden.
     * bei änderungen das E-Mail, Telefon oder Accound Löschen
     * ein E-Mail mit freischaltcode an die ALTE E-Mail-adresse senden
     * die freischaltcode wird zusätzlich als return an die profil.js
     * function codeHolen(.. auch zugesendet(zum Vergleichen)
     *
     * Zugesendete Daten(von profil.js/ codeHolen()...)
     * codetoken = meinen Token
     * codename = was soll versendet, mail oder telefon
     * codevalue = an wem soll versendet: mail-adresse oder telefonnummer(SMS)
     * codevalue: ZURZEIT NICHT GENUTZT, WIRD VERSENDET AN ALTE MAIL-ADRESSE ODER TELEFONNUMMER
     */
    private Usern   myData;
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

        myData      = userService.meineDatenHolen(codetoken);
        alteMail    = myData.getEmail();
        alteTelefon = myData.getTelefon();

        code = methodenService.aktivierungCode();
        //System.out.println("SettingController Zeile:300 "+ codename+ "/" +codevalue+ "/" + alteMail +"/"+ alteTelefon + "/" + code);

        /* mail mit code an alte E-Mail-Adresse vesenden */
        if (alteMail != null && !alteMail.isBlank()){

            String betreff      = "Deine Zugangscode zur Anmeldung";
            String sendeMessage = "hier erhalten Sie ihre Messenger Aktivierung Code\\n" +code+
                    "\\n Gültigkeit dauert nur für diese sitzung \\n \\n mit Freundlichen Grüßen \\n Ihr Team Bote ";
            mail = methodenService.mailSenden(alteMail, betreff, sendeMessage);
            if (mail.equals("nomail")){
                return "nomail";
            }
        }
        /* sms mit code an die alte Telefonnummer versenden */
        if (alteTelefon != null && !alteTelefon.isBlank()){
            String smsText = "Deine Zugangscode zur Anmeldung bei Bote \\n \\n" + code+
                    " \\n \\n mit Freundlichen Grüßen \\n Ihr Team Bote ";
            telefon = methodenService.smsSenden(alteTelefon, smsText);
            if (telefon.equals("nosms")){
                return "nosms";
            }
        }

        /*
         *  code an profil.js/codeHolen()... senden
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
    public String accountAbmelden(@RequestParam(value = "abmeldentoken") String token)
    {
        datum   = methodenService.deDatum();
        outlog  = sessionService.letztenoutlogUpdate(datum, token);

        //System.out.println("Account Abmelden: " +datum +"/"+ outlog);

        return "abgemeldet";
    }



    /**
     *   Account Loschen
     */
    private Usern           userDaten;
    private String          userPseudonym;
    private String          userName;
    private String          userVorname;
    private List<String>    alleMeineMessageToken;
    private String          nameBild;
    private Path            fileName;
    private int             profilBildGeloscht;
    private String          phoneGeloscht;
    private int             countAlleMessage;
    private String          countEntryGeloscht;
    private String          freundeGeloscht;
    private String          messagesGeloscht;
    private String          userGeloscht;
    private String          sessionGeloscht;
    private int             cookieGeloscht;
    private int             sessionPluCookie;
    @PostMapping(value = "/accountloschen")
    public String accountLoschen(@CookieValue(value = "userid", required = false) String cookie,
                                 @RequestParam(value = "token", required = false) String token,
                                 HttpServletResponse response, Model model)
    {
        // Meine Daten ermiteln
        userDaten       = userService.meineDatenHolen(token);
        userPseudonym   = userDaten.getPseudonym();
        userName        = userDaten.getName();
        userVorname     = userDaten.getVorname();
        // finden in H2 das gleiche messageToken wie von meinem Chat-Freund
        alleMeineMessageToken = freundeService.freundeSuchen(String.valueOf(cookie))
                .stream()
                .map(Freunde::getMessagetoken).collect(Collectors.toList());

        /* 0 */     /* Bild aus dem profilBild Löschen */
        nameBild = "profilbild/"+token+".png";
        fileName = Paths.get(nameBild);
        try {
            Files.delete(fileName);
            profilBildGeloscht = 1;
        } catch (IOException e) {
            //e.printStackTrace();
            profilBildGeloscht = 0;
        }

        /* 1 */    /* 100% PHONE Tabbelle leeren, return int: bei leer 0 */
        phoneGeloscht = phoneService.allUserTelfonatLoschen(token);

        /* 2 */     /* 100% COUNTENTRY Tabelle leeren, return int: bei leer 0 */
        countAlleMessage    = entryService.incrementMessageCounter(token);
        countEntryGeloscht = entryService.countEntryLoschen(token);

        /* 3 */     /* 100% FREUNDE Tabelle leeren, return int: bei leer 0  */
        freundeGeloscht = freundeService.allFreundeLoschen(alleMeineMessageToken);

        /* 4 */     /* 100% MESSAGES Tabelle leeren, return int: bei leer 0 */
        messagesGeloscht = messageService.allMessagesLoschen(alleMeineMessageToken);

        /* 5 */     /* 100% USERN Tabelle leeren, return int: bei leer 0 */
        userGeloscht = userService.userLoschen(token);

        /* 6 */     /* 100% SESSION Tabelle leeren, return int: bei leer 0 */
        sessionGeloscht = sessionService.allUserSessionLoschen(token);

        /* 7 */     /* 100% cookie löschen */
        cookieGeloscht = methodenService.deleteCookie(response);
        sessionPluCookie = Integer.parseInt(sessionGeloscht) + cookieGeloscht;

        model.addAttribute("frName", "accountloschen");
        model.addAttribute("userpseudonym", userPseudonym);
        model.addAttribute("username", userName);
        model.addAttribute("uservorname", userVorname);
        model.addAttribute("usercount", userGeloscht);
        model.addAttribute("profilbildcount", profilBildGeloscht);
        model.addAttribute("messagecount", countAlleMessage);
        model.addAttribute("freundecount", freundeGeloscht);
        model.addAttribute("sessioncount", sessionGeloscht);
        model.addAttribute("phonecount", phoneGeloscht);
        model.addAttribute("cookiecount", sessionPluCookie);

        /*System.out.println("SettingController Zeile: 417, Account Loschen: " + alleMeineMessageToken +"/"+ phoneGeloscht +"/"+ sessionGeloscht + "/"
                + freundeGeloscht +"/"+ messagesGeloscht +"/"+ userGeloscht +"/"+ cookieGeloscht+"/"+ countAlleMessage);*/

        return "/setting :: #ACCOUNTLOSCHENFRAGMENT";
    }

}
