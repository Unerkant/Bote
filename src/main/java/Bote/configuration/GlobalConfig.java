package Bote.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class GlobalConfig {

    @Value("${spring.application.name}")
    String appName;

   /**
    *                                   aktuelle datum ermitteln.
    *   ============================================================
    *   1. Deutsches Format (für die Allgemeine anzeige)
    *   2. US-Format (in Datenbank speichern)
    *   3. User Identifizierungsnummer (14-stellige ID-Nummern aus dem aktuellen Datum)
    *   4. wird von MailController.java & TelefonController.java & FreundeController.java benutzt
    */
    public static String deDatum(){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date de = new Date();
        return format.format(de);
    }

    public static String usDatum(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date us = new Date();
        return format.format(us);
    }

    public static String IdentifikationToken(){
        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyHHmmss");
        Date token = new Date();
        return format.format(token);
    }


   /**
    *               Zufall Code für die Registrierung.
    *   ==========================================================
    *   die Aktivierung Code wird 4-stellige Zahlen erstellt
    *   wird von MailController.java & TelefonController.java benutzt
    */
    public static int aktivierungCode(){
        int min = 1;
        int max = 9;
        int eins = (ThreadLocalRandom.current().nextInt(min, max) + min);
        int zwei = (ThreadLocalRandom.current().nextInt(min, max) + min);
        int drei = (ThreadLocalRandom.current().nextInt(min, max) + min);
        int vier = (ThreadLocalRandom.current().nextInt(min, max) + min);
        int result = Integer.parseInt(eins +""+ zwei +""+ drei +""+ vier) ;
        return result;
    }


   /**
    *                         cookie setzen
    *   ========================================================
    *   cookie.setSecure, funktioniert nicht
    *   Benutzt: MailRegisterController.java Zeile 155
    */
    public static void setCookie(HttpServletResponse response, String name, String value){
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(5000 * 24 * 60 * 60);
        //cookie.setSecure(true);       // funktioniert nicht
        //cookie.setHttpOnly(true);     // Lesen mit javascript ist unmöglich ( geschützt)
        cookie.setPath("/");
        response.addCookie(cookie);
    }

   /**
    *                       cookie abfrage
    *   ========================================================
    *
    *   cookie suche, nach bestimmten Namen
    *   wird fast in Alle Controller benutzt
    */
    public static String getCookie(HttpServletRequest reg) {
        Cookie[] coki = reg.getCookies();
        if (coki != null) {
            for (Cookie c : reg.getCookies()) {
                if (c.getName().equals("userid"))
                    return c.getValue();
            }
        }
        /* wenn cookie leer ist, Ausgabe null */
        return null;
    }

   /**
    *                       Cookie Löschen
    *   ==========================================================
    *   wird in SettingController.java benutzt
    * @return
    */
   public static String deleteCookie(HttpServletResponse response){
       // create a cookie
       Cookie cookie = new Cookie("userid", null);
       cookie.setMaxAge(0);
       //cookie.setSecure(true);
       //cookie.setHttpOnly(true);      // Lesen mit javascript ist unmöglich ( geschützt)
       cookie.setPath("/");

        //add cookie to response
       response.addCookie(cookie);
       return null;
   }



   /**
    *                         mailsender
    *   ===========================================================
    *   + Text + Aktivierung Code + Senden
    *   Benuzt von: MailLoginController.java
    *
    *   mailSender vorbereitung
    *   Text ausführung in HTML Format
    *   Aktivierung Code in Text einbinden
    *
    *   Aktivierung Code an angegebene E-Mail-Adresse versenden
    *   E-Mail: unbekanten@gmail.com (application.properties)
    *
    *   @param emailParam
    *   @param aktivierungCode
    */
    private static JavaMailSender mailSender;
    @Autowired
    private JavaMailSender javamailSender;
    @PostConstruct
    public void init(){
        this.mailSender = javamailSender;
    }

    //@SneakyThrows
    public static String mailSenden(String emailParam, int aktivierungCode) {
        String  title = "no-reply: ";
        String  subject = "leer";
        String  content = "Eine neue Nachricht";
        try {
            MimeMessage mailsenden = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mailsenden, true);
            helper.setFrom("---");
            helper.setTo(emailParam);

            boolean html = true;
            helper.setText("<p>hier erhalten Sie ihre Messenger Aktivierung Code </p>"
                    + "<b>" + aktivierungCode + "</b>"
                    + "<p> Gültigkeit dauert nur für diese sitzung </p>"
                    + "<p>mit Freundlichen Grüßen</p>", true);
            String mailSubject = title + " Ihre Aktivierung Code " + aktivierungCode;
            helper.setSubject(mailSubject);

            mailSender.send(mailsenden);
            return String.valueOf(mailSender.toString());
        }catch (Exception e){
            return "nomail";
        }
    }


   /**
    *                      Senden SMS
    *  ==========================================================
    *  Link von Integration API:(muss nicht angemeldet sein)
    *  https://www.textlocal.com/integrations/api/
    *
    *  Link von API Key: (muss angemeldet sein)
    *  https://control.txtlocal.co.uk/settings/apikeys/
    *  Settings->API Keys
    *
    *  gesendete Message ansehen
    *  https://control.txtlocal.co.uk/reports/history/api/
    *  Reports-> API Messages
    *
    *  Telefonnummer: von England kostenlos SMS- empfangen
    *  https://sms-online.co/de/kostenlos-sms-empfangen/447520635797
    *
    *  leider mit dem Deutsche-Handy-Nummert hatte nicht functioniert
    */
    public static String smsSenden(String telefon, int aktivierCode){
        String  apiKey;
        String  message;
        String  sender;
        String  numbers;
        String  data;
        String  line;
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
        return stringBuffer.toString();

        }catch (Exception e){
            return "nosms";
        }
    }

}
