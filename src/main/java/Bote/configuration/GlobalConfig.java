package Bote.configuration;

import org.springframework.beans.factory.annotation.Value;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;


public class GlobalConfig {

    @Value("${spring.application.name}")
    String appName;

   /**
    *   aktuelle datum ermitteln.
    *
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
    *   Zufall Code für die Registrierung.
    *
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
    *   cookie setzen
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
    *   Cookie abfrage
    *   cookie suche, nach bestimmten Namen
    *
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
    *   Cookie Löschen
    *   wird in MessageController.java benutzt
    */
   public static void deleteCookie(HttpServletResponse response){
       // create a cookie
       Cookie cookie = new Cookie("userid", null);
       cookie.setMaxAge(0);
       //cookie.setSecure(true);
       //cookie.setHttpOnly(true);      // Lesen mit javascript ist unmöglich ( geschützt)
       cookie.setPath("/");

        //add cookie to response
       response.addCookie(cookie);
   }

}
