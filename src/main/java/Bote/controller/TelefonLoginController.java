package Bote.controller;

import Bote.configuration.GlobalConfig;
import Bote.model.Usern;
import Bote.service.UserService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
public class TelefonLoginController {

    private Usern   meineDaten;
    private String  aktuelleDatum;
    private String  identToken;
    private int     aktivierCode;
    private String  telefon;
    private String  requestNummer;
    private String  internationaleNummer;
    private String  telPseud;
    private String  telPseudonym;
    private String  smsSenden;

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserService userService;

    @SneakyThrows
    @GetMapping(value = "/login/telefonregister")
    public String login(@CookieValue(value = "userid", required = false) String userId){

        meineDaten = userService.findeUserToken(userId);
        return (meineDaten == null ? "/login/telefonregister" : "/messenger");
    }

    @PostMapping("/login/telefonregister")
    public String telefonRegister(HttpServletRequest request, Model model, RedirectAttributes redidAttr){

        /* **************************************************** */
        /*                   Daten Sammeln                      */
        /*  ==================================================  */
        /*  1. Aktuelle Datum                                   */
        /*  2. Identifikation Nummer (Token)                    */
        /*  3. Aktivierung Code                                 */
        /*                                                      */
        /*  Telefon Nummer mit Internationale vorwahl           */
        /*                                                      */
        /*  Pseudonym Name erstellen (Letzte 2 Zahlen)          */
        /*                                                      */
        /* **************************************************** */
        aktuelleDatum = GlobalConfig.deDatum();
        identToken = GlobalConfig.IdentifikationToken();
        aktivierCode = GlobalConfig.aktivierungCode();

        telefon = request.getParameter("telefon");
        requestNummer = request.getParameter("vorwahl") + request.getParameter("telefon");
        internationaleNummer = requestNummer.replaceAll("[^+0-9]","");

        telPseud = telefon.substring(telefon.length() -2);
        telPseudonym = telPseud.toUpperCase(Locale.ROOT);

        /* ********************************************************* */
        /* SMS Senden, ausgelagert in GlobalConfig.java              */
        /* ********************************************************* */
        String smsText = "Deine Zugangscode zur Anmeldung bei Bote \\n \\n" + aktivierCode+
                " \\n \\n mit Freundlichen Grüßen \\n Ihr Team Bote ";
        smsSenden = GlobalConfig.smsSenden(telefon, smsText);
        if (smsSenden == "nosms"){
            redidAttr.addFlashAttribute("telefonfehler", "Error: ");
            return "redirect:/login/telefonlogin";
        }else {
            logger.info("SMS Sender: " + smsSenden);
        }


        /* ******************************************************* */
        /*                  Daten Weitergeben                      */
        /*  =====================================================  */
        /*  Daten weiter an telefonregister.html senden            */
        /*                                                         */
        /*                                                         */
        /*  Weiterleitung zu Registrierung Code Überprüfen         */
        /*                                                         */
        /* ******************************************************* */

        model.addAttribute("sendeDatum", aktuelleDatum );
        model.addAttribute("sendeMail", "");
        model.addAttribute("sendeName", "");
        model.addAttribute("sendeVorname", "");
        model.addAttribute("sendePseud", telPseudonym);
        model.addAttribute("sendeRole", "default");
        model.addAttribute("sendeTelefon", internationaleNummer);
        model.addAttribute("sendeToken", identToken);
        model.addAttribute("sendeCode", aktivierCode);

        logger.info("Telefon Controller: " + requestNummer+"/"+ aktivierCode);
        return "/login/telefonregister";
    }
}
