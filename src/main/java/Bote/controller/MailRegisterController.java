package Bote.controller;

import Bote.configuration.GlobalConfig;
import Bote.model.Session;
import Bote.model.User;
import Bote.service.SessionService;
import Bote.service.UserService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class MailRegisterController {

    private String  saveDatum;
    private String  saveMail;
    private String  savePseudonym;
    private String  saveToken;
    private int     mailCode;
    private String  ersteCode;
    private String  zweiteCode;
    private String  dritteCode;
    private String  vierteCode;
    private int     tippCode;
    private User    altUser;
    private String  uniCode = "&#x22EF;";

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @SneakyThrows
    @GetMapping(value = "/login/mailsuccess")
    public String login(@CookieValue(value = "userid", required = false) String userId){

        return (userId == null ? "/login/mailsuccess" : "/messenger");
    }


    @PostMapping("/login/mailsuccess")
    public String registerSuccess(HttpServletRequest request, HttpServletResponse response,
                                  Model model, RedirectAttributes redAttr)
    {

       /*
        * -----------------------------------------------------------
        *   Daten sammeln
        * -----------------------------------------------------------
        *
        *   1. Daten aus dem mailregister.html holen
        *   2. die Tipp Code in eine variable speichern (tippCode)
        *   3. ...
        */
        saveDatum = request.getParameter("regDatum");
        saveMail = request.getParameter("regMail");
        savePseudonym = request.getParameter("regPseudonym");
        saveToken = request.getParameter("regToken");
        mailCode = Integer.parseInt(request.getParameter("regCode"));

        ersteCode = request.getParameter("codeEins");
        zweiteCode = request.getParameter("codeZwei");
        dritteCode = request.getParameter("codeDrei");
        vierteCode = request.getParameter("codeVier");

        tippCode = Integer.parseInt(ersteCode + zweiteCode + dritteCode + vierteCode);

       /*
        * -----------------------------------------------------------
        *   Fehler in mailregister.html ausgeben
        * -----------------------------------------------------------
        *
        *   1. bei Falsche eingegebene Code redirect:/ ausführen (die Seite wird aktualisiert)
        *   2. die Daten gehen verloren, muss erneut zugesendet sein
        *   3. die daten erneut zu mailregister.html senden, mit addFlashAttr...
        *   4. ansonsten bei erneute Code Prüfen mit den leeren variablen(mailCode) wird die ERROR ausgelöst
        *
        */

        //logger.info(mailCode+" /mail register/ " + tippCode);
        if (mailCode != tippCode) {
            redAttr.addFlashAttribute("mailFehler", "Registrierungscode scheint falsch zu sein ...(richtige)->" + mailCode+
                    "bitte überprüfen Sie Ihr Email-Postfach!");
            redAttr.addFlashAttribute("returnDatum", saveDatum);
            redAttr.addFlashAttribute("returnMail", saveMail);
            redAttr.addFlashAttribute("returnPseudonym", savePseudonym);
            redAttr.addFlashAttribute("returnToken", saveToken);
            redAttr.addFlashAttribute("returnCode", mailCode);
            //model.addAttribute("returnMail", saveMail);
            return "redirect:/login/mailregister";
        }

       /*
        * -----------------------------------------------------------
        *   Daten in H2 Datenbank speichern
        * -----------------------------------------------------------
        *
        *   1. zuerst wir auf vorhandene E-Mail überprüft
        *   2. wenn keine cookie & E-Mail vorhanden sind,
        *      wird in H2 Datenbank Tabelle 'User' & 'Session'
        *      neue Date eingetragen (IF-Abfrage) und schließlich
        *      die NEUEN Daten auf die nächste Seite angezeigt +
        *      neue cookie gesetzt (mit neuen userId)
        *   3. wenn eine E-Mail vorhanden ist dann werden
        *      die ALTE User Daten auf die nächste Seite angezeigt
        *      und schließlich cookie gesetzt (mit alte userId)
        *   4. Registrierung oder einloggen abgeschlossen
        *
        *   5. Dopellte speicherung fehlgeschlagen:
        *      in Datenbak H2 speichert jede 2 id (z.b.s 12 und gleich 14)
        */

        altUser = userService.sucheMail(saveMail);
        if (altUser == null){

            /* H2 Datenbank Tabelle:User */
            User newUser = new User();
            newUser.setBild("");
            newUser.setDatum(saveDatum);
            newUser.setEmail(saveMail);
            newUser.setName("");
            newUser.setPseudonym(savePseudonym);
            newUser.setRole("default");
            newUser.setTelefon("");
            newUser.setToken(saveToken);
            newUser.setVorname("");

            userService.saveNewUser(newUser);

            /* H2 datenbank Tabelle:Session */
 /*           Session logDaten = new Session();
            logDaten.setDatum(saveDatum);
            logDaten.setLetztenlogin(saveDatum);
            logDaten.setLetztenoutlog("");
            logDaten.setOther("");
            logDaten.setToken(saveToken);
*/
            //sessionService.saveLogDaten(logDaten);
        }

            /* User Daten in mailsuccess ausgeben */
            model.addAttribute("registerDatum", (altUser != null) ? altUser.getDatum() : saveDatum);
            model.addAttribute("registerToken", (altUser != null) ? altUser.getToken() : saveToken);
            model.addAttribute("registerName", (altUser != null) ? altUser.getName() : uniCode);
            model.addAttribute("registerPseudonym", (altUser != null) ? altUser.getPseudonym() : savePseudonym );
            model.addAttribute("registerMail", (altUser != null) ? altUser.getEmail() : saveMail);
            model.addAttribute("registerTelefon", (altUser != null) ? altUser.getTelefon() : uniCode);
            model.addAttribute("registerCookie", (altUser != null) ? altUser.getToken() : uniCode);

        /* cookie "userid" setzen */
        GlobalConfig.setCookie(response, "userid", (altUser != null) ? altUser.getToken() : saveToken);

        logger.info("MailRegisterController: " + altUser);
        return "/login/mailsuccess";
    }

}
