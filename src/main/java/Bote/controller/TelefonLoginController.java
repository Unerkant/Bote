package Bote.controller;

import Bote.configuration.GlobalConfig;
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

/* SMS Import */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

@Controller
public class TelefonLoginController {

    private String  aktuelleDatum;
    private String  identToken;
    private int     aktivierCode;
    private String  telefon;
    private String  requestNummer;
    private String  internationaleNummer;
    private String  telPseud;
    private String  telPseudonym;
    private String  apiKey;
    private String  message;
    private String  sender;
    private String  numbers;
    private String  data;
    private String  line;

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserService userService;

    @SneakyThrows
    @GetMapping(value = "/login/telefonregister")
    public String login(@CookieValue(value = "userid", required = false) String userId){

        return (userId == null ? "/login/telefonregister" : "/messenger");
    }

    @PostMapping("/login/telefonregister")
    public String telefonRegister(HttpServletRequest request, Model model, RedirectAttributes redidAttr){

        /* **************************************************** */
        /*                   Daten Sammeln                      */
        /*  ==================================================  */
        /*  1. Aktuelle Datum                                   */
        /*  2. Identifikation Nummer (Token)                           */
        /*  3. Aktivierung Code                                 */
        /*                                                      */
        /*  Telefon Nummer mit Internationale vorwahl           */
        /*                                                      */
        /*  Pseudonym Name erstellen (Letzte 2 Zahlen)           */
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


        /* **************************************************************** */
        /*                      Senden SMS                                  */
        /*  ==============================================================  */
        /*  Link von Integration API:(muss nicht angemeldet sein)           */
        /*  https://www.textlocal.com/integrations/api/                     */
        /*                                                                  */
        /*  Link von API Key: (muss angemeldet sein)                        */
        /*  https://control.txtlocal.co.uk/settings/apikeys/                */
        /*  Settings->API Keys                                              */
        /*                                                                  */
        /*  gesendete Message ansehen                                       */
        /*  https://control.txtlocal.co.uk/reports/history/api/             */
        /*  Reports-> API Messages                                          */
        /*                                                                  */
        /*  Telefonnummer: von England, kostenlos SMS- empfangen            */
        /*  https://sms-online.co/de/kostenlos-sms-empfangen/447520635797   */
        /*                                                                  */
        /* **************************************************************** */
                try {
                    // Construct data
                    apiKey = "apikey=" + "NmI2NTc4MzM3ODQzNmU0ZTYxNTI1MzZmNTk0ODZlNGI=";
                    message = "&message=" + "die Aktivierung Code: " + aktivierCode;
                    sender = "&sender=" + "no-reply";
                    numbers = "&numbers=" + telefon;

                    // Send data
                    HttpURLConnection conn = (HttpURLConnection) new URL("https://api.txtlocal.com/send/?").openConnection();
                    data = apiKey + numbers + message + sender;
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
                    conn.getOutputStream().write(data.getBytes("UTF-8"));
                    final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    final StringBuffer stringBuffer = new StringBuffer();

                    while ((line = rd.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                    rd.close();

                    logger.info("SMS Sender: " + stringBuffer.toString());
                } catch (Exception e) {

                    redidAttr.addFlashAttribute("telefonfehler", "Error: " + e);
                    return "redirect:/login/telefonlogin";
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

        logger.info("Telefon Controller: " + telPseudonym +"/"+ aktivierCode);
        return "/login/telefonregister";
    }
}
