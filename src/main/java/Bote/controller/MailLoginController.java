package Bote.controller;

import Bote.model.Usern;
import Bote.service.MethodenService;
import Bote.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;


@Controller
public class MailLoginController {

    @Autowired
    private MethodenService methodenService;
    @Autowired
    private UserService userService;

    private Usern   meineDaten;
    private String  aktuelleDatum;
    private String  tokenNummer;
    private int     aktivierungCode;
    private String  userCookie;
    private String  emailParam;
    private String  pseu;
    private String  pseudonym;
    private String  mailSenden;


    @GetMapping(value = "/login/mailregister")
    public String login(@CookieValue(value = "userid", required = false) String userId){

        meineDaten = userService.meineDatenHolen(userId);
        System.out.println("MailLoginController @GetMapping: " + meineDaten);

        return (meineDaten == null ? "/login/mailregister" : "/messenger");
    }



    @PostMapping("/login/mailregister")
    public String mailregister(HttpServletRequest request, Model model ){

   /**
    *   Komponenten holen (Quelle: GlobalConfig)
    *
    *   Aktuelle Datum
    *   Identifikationsnummer (neue userId)
    *   Aktivierung Code, 4-stellig,
    *   user cookie (userid), wenn vorhanden ist
    *
    */
        aktuelleDatum =     methodenService.deDatum();
        tokenNummer =       methodenService.IdentifikationToken();
        aktivierungCode =   methodenService.aktivierungCode();
        /* Leere cookie: Ausgabe null */
        userCookie = methodenService.getCookie(request);
        //logger.info("MailController: " + aktuelleDatum + "/" +tokenNummer + "/" +aktivierungCode + "/" + userCookie);


   /**
    *   Parameter Auslesen, Pseudonym erstellen
    *
    *   1. E-Mail auslesen Quelle: maillogin.html
    *   2. Pseudonym erstellen: erste 2 Buchstaben von dem E-Mail
    *
    */
        //name = request.getParameter("name");
        emailParam = request.getParameter("email");
        //subject = request.getParameter("subject");
        //content = request.getParameter("content");
        pseu = emailParam.substring(0, emailParam.length() - emailParam.length() + 2);
        pseudonym = pseu.toUpperCase(Locale.ROOT);


   /**
    *   Mail Sender, ausgelagert in MethodeService.java Zeile: 120
    *   zugesendet 1. mail + mail betreff + text message
    *   response- mailSenden: org.springframework.mail.javamail.JavaMailSenderImpl@3df04fa1
    */

   String betreffParam      = "Deine Zugangscode zur Anmeldung";
   String messageParam = "hier erhalten Sie ihre Messenger Aktivierung Code\\n" +aktivierungCode+
           "\\n Gültigkeit dauert nur für diese sitzung \\n \\n mit Freundlichen Grüßen \\n Ihr Team Bote ";
    mailSenden = methodenService.mailSenden(emailParam, betreffParam, messageParam);
    if (mailSenden == null){
        return "redirect:/login/maillogin";
    }
    //logger.info("Mail Senden Controller: " + mailSenden);

   /**
    *   Daten Weitergabe an die Seite mailregister.html
    *
    *   1. Aktuelle Datum
    *   2. Identifikation Nummer
    *   3. Pseudonym Name
    *   4. E-Mail-Adresse
    *   5. Aktivierung Code
    *
    */

        model.addAttribute("title", "registerlogin");
        model.addAttribute("sendDatum", aktuelleDatum);
        model.addAttribute("sendToken", tokenNummer);
        model.addAttribute("sendPseudonym", pseudonym);
        model.addAttribute("sendMail", emailParam);
        model.addAttribute("sendCode", aktivierungCode);
        model.addAttribute("mailInfo", "Vielen Dank, bitte überprüfen Sie Ihr Email-Postfach!\n" +
                "Wir haben Ihnen eine Bestätigungsmail an die von Ihnen angegebene Email-Adresse gesendet. " +
                "Bitte prüfen Sie auch Ihren SPAM-Ordner, falls Sie in den nächsten Minuten keine Bestätigung Code erhalten haben!\n" +
                "\n" +
                "Vielen Dank und mit freundlichen Grüßen!" +
                "\n"+
                "Ihr Bote Team");

        System.out.println("MailLoginController @PostMapping: " + emailParam + "/" + tokenNummer +"/"+ aktivierungCode );
        return "/login/mailregister";

    }

}
