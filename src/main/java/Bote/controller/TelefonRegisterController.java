package Bote.controller;

import Bote.configuration.GlobalConfig;
import Bote.model.Session;
import Bote.model.Usern;
import Bote.service.SessionService;
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
import javax.servlet.http.HttpServletResponse;


@Controller
public class TelefonRegisterController {

    private Usern   meineDaten;
    private String  saveTelefon;
    private String  saveDatum;
    private String  saveName;
    private String  savePseudonym;
    private String  saveToken;
    private int     mailCode;
    private String  ersteCode;
    private String  zweiteCode;
    private String  dritteCode;
    private String  vierteCode;
    private int     tippCode;
    private Usern   altUser;
    private Usern   newUser;
    private String  uniCode = "&#x22EF;";

    @Autowired
    private UserService userService;
    @Autowired
    private SessionService sessionService;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @SneakyThrows
    @GetMapping(value = "/login/telefonsuccess")
    public String login(@CookieValue(value = "userid", required = false) String userId){

        meineDaten = userService.findeUserToken(userId);
        return (meineDaten == null ? "/login/telefonsuccess" : "/messenger");
    }

    @PostMapping("/login/telefonsuccess")
    public String telefonRegister(HttpServletRequest request, HttpServletResponse response,
                                  RedirectAttributes redicAttr, Model model){

       /*
        *   Daten aus den telefonregister.html
        *   eingegebene Code-Zahlen prüfen, bei Falsche eingabe
        *   Seite zurücksetzen und Daten wieder in das Formular speichern
        *
        */
        saveTelefon     = request.getParameter("telTelefon");
        saveDatum       = request.getParameter("telDatum");
        saveName        = request.getParameter("telName");
        savePseudonym   = request.getParameter("telPseudonym");
        saveToken       = request.getParameter("telToken");
        mailCode        = Integer.parseInt(request.getParameter("telCode"));

        ersteCode = request.getParameter("telCodeEins");
        zweiteCode = request.getParameter("telCodeZwei");
        dritteCode = request.getParameter("telCodeDrei");
        vierteCode = request.getParameter("telCodeVier");

        tippCode = Integer.parseInt(ersteCode + zweiteCode + dritteCode + vierteCode);
        //logger.info("Telefon Controller" + emailsCode + "/" + tippsCode);

        if (mailCode != tippCode){
            redicAttr.addFlashAttribute("telefonFehler", "Registrierungscode scheint falsch zu sein ...(richtige)->" + mailCode +
                    "Telefon Code Link: https://sms-online.co/de/kostenlos-sms-empfangen/447520635797"  );
            redicAttr.addFlashAttribute("returnTelTelefon", saveTelefon);
            redicAttr.addFlashAttribute("returnTelDatum", saveDatum);
            redicAttr.addFlashAttribute("returnTelName", saveName);
            redicAttr.addFlashAttribute("returnTelPseudonym", savePseudonym);
            redicAttr.addFlashAttribute("returnTelToken", saveToken);
            redicAttr.addFlashAttribute("returnTelCode", mailCode);
            
            return "redirect:/login/telefonregister";
        }


        /*
         * -----------------------------------------------------------
         *   Daten in Datenbank speichern
         * -----------------------------------------------------------
         *
         *  1. Telefonnummer prüfen
         *  2. Telefonnummer nicht vorhanden, als neue User speichen (IF)
         *  3. in die Tabelle: Session einloggen Datum eintragen
         *
         *  ACHTIUNG: in die Tabelle 'Session' kann zurzeit keine Daten speichen
         *  meine meinung nach das H2 - Datenbank zulässt keine 2 sql-verbindungen
         *
         *  4. Registrierte und gespeicherte Daten von neuer User
         *     in die telefonsuccess.html Seite ausgeben
         *  5. cookie setzen
         *
         */

        saveTelefon = saveTelefon.trim().replaceAll("\\s+", "").replace("+","");
        altUser = userService.sucheTelefon(saveTelefon);
        if (altUser == null) {
            newUser = new Usern();
            newUser.setToken(saveToken);
            newUser.setDatum(saveDatum);
            newUser.setBild("");
            newUser.setPseudonym(savePseudonym);
            newUser.setName("");
            newUser.setVorname("");
            newUser.setEmail("");
            newUser.setTelefon(saveTelefon);
            newUser.setRole("default");
            newUser.setOther("");

            userService.saveNewUser(newUser);

            /* H2 datenbank Tabelle:Session */
            Session logDaten = new Session();
            logDaten.setDatum(saveDatum);
            logDaten.setLetztenlogin(saveDatum);
            logDaten.setLetztenoutlog("");
            logDaten.setOther("");
            logDaten.setToken(String.valueOf(saveToken));

            sessionService.saveLogDaten(logDaten);
        }else {
            /* Datum Update: Tabelle Session, spalte: letztenlogin */
            sessionService.letzteloginUpdate(saveDatum, String.valueOf(altUser.getToken()));
        }


        /* User Daten in mailsuccess ausgeben */
        model.addAttribute("registerDatum", (altUser != null) ? altUser.getDatum() : saveDatum);
        model.addAttribute("registerToken", (altUser != null) ? altUser.getToken() : saveToken);
        model.addAttribute("registerName", (altUser != null) ? altUser.getName() : uniCode);
        model.addAttribute("registerPseudonym", (altUser != null) ? altUser.getPseudonym() : savePseudonym );
        model.addAttribute("registerMail", (altUser != null) ? altUser.getEmail() : uniCode);
        model.addAttribute("registerTelefon", (altUser != null) ? altUser.getTelefon() : saveTelefon);
        model.addAttribute("registerCookie", (altUser != null) ? altUser.getToken() : uniCode);

        /* cookie "userid" setzen */
        GlobalConfig.setCookie(response, "userid", String.valueOf((altUser != null) ? altUser.getToken() : saveToken));
        logger.info("Telefon Register Controller/ altUser: " + altUser);
        return "/login/telefonsuccess";
    }
}
