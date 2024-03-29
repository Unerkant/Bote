package Bote.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * den 17.05.2023
 */

@Service
public class MethodenService {


    /**
     *   aktuelle datum ermitteln.
     *   ============================================================
     *   1. Deutsches Format (für die Allgemeine anzeige)
     *   2. US-Format (in Datenbank speichern)
     *   3. User Identifizierungsnummer (14-stellige ID-Nummern aus dem aktuellen Datum)
     *   4. wird von MailController.java & TelefonController.java & FreundeController.java benutzt....
     *   5. aktuelleTag wird von CountEntryService.java benutzt
     */
    public String deDatum(){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date de = new Date();
        return format.format(de);
    }



    /**
     * zurzeit nicht genutzt,
     * für MySql-Datenbank gedacht, weil die datum wird in US-Format gespeichert
     * @return
     */
    public String usDatum(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date us = new Date();
        return format.format(us);
    }



    /**
     * benutzt von CountEntryService
     * @return
     */
    public String aktuellTag(){
        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
        Date tag = new Date();
        return format.format(tag);
    }



    /**
     * IdentifikationToken ist einen 14-stelliges eindeutiges nummer aus Datum zusammen gestellt ohne punkten,
     * Tag + Monat + Jahr + Stunden + Minuten + Sekunden
     * @return
     */
    public String IdentifikationToken(){
        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyHHmmss");
        Date token = new Date();
        return format.format(token);
    }



    /**
     *   Zufall Code für die Registrierung.
     *   ==========================================================
     *   die Aktivierung Code wird 4-stellige Zahlen erstellt
     *   wird von MailController.java & TelefonController.java benutzt
     */
    public int aktivierungCode(){
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
     *   cookie setzen
     *   ========================================================
     *   cookie.setSecure, funktioniert nicht
     *   benutzt: MailRegisterController.java Zeile 155
     */
    public void setCookie(HttpServletResponse response, String name, String value){
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(5000 * 24 * 60 * 60);
        //cookie.setSecure(true);       // funktioniert nicht
        //cookie.setHttpOnly(true);     // Lesen mit javascript ist unmöglich ( geschützt)
        cookie.setPath("/");
        response.addCookie(cookie);
    }



    /**
     *   cookie abfrage
     *   ========================================================
     *
     *   cookie suche, nach bestimmten Namen
     *   wird fast in Alle Controller benutzt
     */
    public String getCookie(HttpServletRequest reg) {
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
     *   Cookie Löschen
     *   ==========================================================
     *   wird in SettingController.java benutzt
     * @return
     */
    public int deleteCookie(HttpServletResponse response){
        // create a cookie
        Cookie cookie = new Cookie("userid", "");
        cookie.setMaxAge(0);

        //add cookie to response
        response.addCookie(cookie);
        return 1;
    }




    /**
     *   Strato mailsender
     *   ===========================================================
     *   Mail-Sender ist von eigenes Produktion, programiert von Paul-Junior
     *   Quell-Code liegt auf dem Strato-Server...
     *   per Request an Strato-Server zugesendet:
     *       a. appId:           Bote
     *       b. keyValue:        73b7f892-baa0-4b8b-b9af-2b72e1abf7ef
     *       c. emailAddress:    von Text-Field ausgelesen
     *       d. betreff:         E-Mail Betreff
     *       e. textMessage      E-Mail text von message
     *
     *   ACHTUNG: die notwendigen 3 Parameter zu Methode senden
     *       a. E-Mail-Adresse
     *       b. betreff eine E-Mail
     *       c. text message
     *
     *   Benutzt von:
     *       1. MailLoginController.java Zeile 90
     *       2. SettingController.java Zeile: 305
     *       3. ApiMailController/ @PostMapping(value = "/mailApi") Zeile: 68
     *
     *   Einladung oder Aktivierungscode an angegebene E-Mail-Adresse versenden
     *   (code wird in textMessage interagiert)
     *
     *
     *   @param neuUserMail
     *   @param betreff
     *   @param textMessage
     */
    public String mailSenden(String neuUserMail, String betreff, String textMessage){

        String appid        = "Bote";
        String keyvalue     = "73b7f892-baa0-4b8b-b9af-2b72e1abf7ef";

        //String url = "http://h2981507.stratoserver.net:8090/sendEmail";
        String url = "http://h2983397.stratoserver.net:8090/sendEmail";
        String json = "{ \"appKey\":{\"appId\":\""+appid+"\", \"keyValue\":\""+keyvalue+"\"}," +
                "\"emailAddress\":\""+neuUserMail+"\"," +
                "\"subject\":\""+betreff+"\", " +
                "\"message\": \""+textMessage+"\" }";

        // send a JSON data
        HttpResponse<String> response = null;

        try {
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return String.valueOf(response.statusCode());

        }catch (IOException ex){
            //ex.printStackTrace();
            return "nomail";
        }catch (InterruptedException ie){
            //ie.printStackTrace();
            return "nomail";
        }
    }



    /**
     *                      Senden SMS
     *  ==========================================================
     *       ACHTUNG: KEINE CREDITS BEI TEXTLOCAL
     *       https://control.txtlocal.co.uk
     *
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
    public String smsSenden(String telefon, String smsText){
        String  apiKey;
        String  message;
        String  sender;
        String  numbers;
        String  data;
        String  line;
        try {
            // Construct data
            apiKey = "apikey=" + "NmI2NTc4MzM3ODQzNmU0ZTYxNTI1MzZmNTk0ODZlNGI=";
            message = "&message=" + smsText;
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
