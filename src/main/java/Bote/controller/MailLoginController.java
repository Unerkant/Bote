package Bote.controller;

import Bote.configuration.GlobalConfig;
import Bote.model.User;
import Bote.service.UserService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;


@Controller
public class MailLoginController {

    private User    meineDaten;
    private String  aktuelleDatum;
    private String  tokenNummer;
    private int     aktivierungCode;
    private String  userCookie;
    private String  name = "no-reply: ";
    private String  emailParam;
    private String  subject = "leer";
    private String  content = "Eine neue Nachricht";
    private String  pseu;
    private String  pseudonym;
    private String  mailSubject;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;
    @Autowired
    private JavaMailSender mailSender;

    @SneakyThrows
    @GetMapping(value = "/login/mailregister")
    public String login(@CookieValue(value = "userid", required = false) Long userId){

        meineDaten = userService.findeUserToken(userId);
        logger.info("MailLoginController @GetMapping" + meineDaten);
        return (meineDaten == null ? "/login/mailregister" : "/messenger");
    }

    @SneakyThrows
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
        aktuelleDatum = GlobalConfig.deDatum();
        tokenNummer = GlobalConfig.IdentifikationToken();
        aktivierungCode = GlobalConfig.aktivierungCode();
        /* Leere cookie: Ausgabe null */
        userCookie = GlobalConfig.getCookie(request);
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
    *   mailsender + Text + Aktivierung Code + Senden
    *
    *   mailSender vorbereitung
    *   Text ausführung in HTML Format
    *   Aktivierung Code in Text einbinden
    *
    *   Aktivierung Code an angegebene E-Mail-Adresse versenden
    *   E-Mail: unbekanten@gmail.com (application.properties)
    */
        MimeMessage mailsenden = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailsenden, true);
        helper.setFrom("---");
        helper.setTo(emailParam);

        boolean html = true;
        helper.setText( "<p>hier erhalten Sie ihre Messenger Aktivierung Code </p>"
                +"<b>" + aktivierungCode + "</b>"
                +"<p> Gültigkeit dauert nur für diese sitzung </p>"
                +"<p>mit Freundlichen Grüßen</p>", true);
        mailSubject = name + " Ihre Aktivierung Code " + aktivierungCode;
        helper.setSubject(mailSubject);

        mailSender.send(mailsenden);


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
                "Ihr Messenger Team");

        logger.info("MailLoginController @PostMapping: " + emailParam + "/" + tokenNummer +"/"+ aktivierungCode );
        return "/login/mailregister";

    }

}
