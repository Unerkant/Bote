package Bote.controller.Api;

import Bote.configuration.GlobalConfig;
import Bote.model.Session;
import Bote.model.Usern;
import Bote.service.MethodenService;
import Bote.service.SessionService;
import Bote.service.UserService;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Locale;

/**
 * Den 21.09.2022
 */

@Controller
public class ApiTelefonController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MethodenService methodenService;
    @Autowired
    private UserService userService;
    @Autowired
    private SessionService sessionService;


    private int     telefonAktivierungsCode;
    private String  newUserTelefon;
    private int     kodeZugesendet;
    private String  telZugesendet;
    private String  neuToken;
    private String  neuDatum;
    private String  neuPseudonym;
    private Usern   altUser;
    private String  dataResponse;


    /**
     * Telefonnummer zugesendet von BoteFX
     * Quelle: TelefonLoginController/ Zeile: 99, GlobalApiRequest.requestAPI(link, data);
     *
     * eine SMS mit Aktivierungscode an dies Telefon senden
     *
     * @param telefonZugesendet
     * @return
     */
    @PostMapping(value = "/telefonApi")
    public ResponseEntity<String> apiTelefon(@RequestBody String telefonZugesendet){

        telefonAktivierungsCode = methodenService.aktivierungCode();
        JSONObject obj = new JSONObject(telefonZugesendet);
        newUserTelefon = (String) obj.get("neuUserTelefon");

        //String apiMsmSender = GlobalConfig.smsSenden(newUserTelefon, telefonAktivierungsCode);
        String apiMsmSender = "ok";   // nur für den Test, kann gelöscht sein

        if (apiMsmSender == "nosms"){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }


    /**
     * Aktivierung Code vergleichen/prüfen
     * die angegebenen Codes von User wir mit dem MSM versendetet, Code verglichen
     * bei richtigem Code:
     *  1. Neuer User: alle vorhandenen Daten ins MySql Database gespeichert
     *  2. Alter User: die Daten aus dem Mysql gelesen und als response.body zurückgesendet
     *
     * @param datenZugesendet
     * @return
     */
    @PostMapping(value = "/kodeApi")
    public @ResponseBody ResponseEntity<String> apiKode(@RequestBody String datenZugesendet){
        /* Daten Sammeln */
        JSONObject object = new JSONObject(datenZugesendet);
        kodeZugesendet  = object.getInt("kode");
        telZugesendet   = object.getString("telefon");
        neuToken = methodenService.IdentifikationToken();
        neuDatum = methodenService.deDatum();
        neuPseudonym = telZugesendet.substring(telZugesendet.length() -2);
        neuPseudonym = neuPseudonym.toUpperCase(Locale.ROOT);

        /**
         *  Später Löschen
         *  Funktioniert nur bei Falsche Aktivierung Code eingabe.
         *
         *  Aktivierung Code zurück zu BoteFx senden wenn eine Falsche Code eingegeben ist,
         *  dient nur für Versuch Zwecken
         */
        String jsonTest = "{\"testCode\":"+telefonAktivierungsCode+"}";
        /* Ende Später Löschen */


        /* Aktivierungs Code Prüfen */
        if (kodeZugesendet == telefonAktivierungsCode){
            /* nach Telefon in Mysql Suchen */
            altUser = userService.sucheTelefon(telZugesendet);
            if (altUser == null){
                Usern neuUser = new Usern();
                /* Neuer User Registrieren */
                neuUser.setBild("");
                neuUser.setDatum(neuDatum);
                neuUser.setEmail("");
                neuUser.setName("");
                neuUser.setOther("");
                neuUser.setPseudonym(neuPseudonym);
                neuUser.setRole("default");
                neuUser.setTelefon(telZugesendet);
                neuUser.setToken(neuToken);
                neuUser.setVorname("");
                userService.saveNewUser(neuUser);

                /* neuer User Session anlegen  */
                Session sessionData = new Session();
                sessionData.setDatum(neuDatum);
                sessionData.setLetztenlogin(neuDatum);
                sessionData.setLetztenoutlog("");
                sessionData.setOther("");
                sessionData.setToken(neuToken);
                sessionService.saveLogDaten(sessionData);

            } else {
                /* Datum Update: Tabelle Session, spalte: letztenlogin */
                sessionService.letzteloginUpdate(neuDatum, String.valueOf(altUser.getToken()));
            }

            /**
             *  Alten User Daten zurück an:
             *  BoteFx -> MailRegisterController.java -> public void codePrufen(ActionEvent event){}
             */
            dataResponse =  "{\"bildResponse\":\"" +   ( altUser != null ? altUser.getBild()  : "" )+"\", " +
                    "\"datumResponse\":\"" +   ( altUser != null ? altUser.getDatum()         : neuDatum )+"\", " +
                    "\"mailResponse\":\"" +    ( altUser != null ? altUser.getEmail()         : telZugesendet )+ "\", " +
                    "\"nameResponse\":\"" +    ( altUser != null ? altUser.getName()          : "" )+ "\", " +
                    "\"otherResponse\":\"" +   ( altUser != null ? altUser.getOther()         : "" )+ "\", " +
                    "\"pseudonymResponse\":\""+( altUser != null ? altUser.getPseudonym()     : neuPseudonym )+ "\", " +
                    "\"roleResponse\":\"" +    ( altUser != null ? altUser.getRole()          : "default" )+ "\", " +
                    "\"telefonResponse\":\"" + ( altUser != null ? altUser.getTelefon()       : "" )+"\", " +
                    "\"tokenResponse\":\""+    ( altUser != null ? altUser.getToken()         : neuToken )+"\", " +
                    "\"vornameResponse\":\""+  ( altUser != null ? altUser.getVorname()       : "" )+"\" }";

            /* Return an BoteFX/TelefonRegisterController Zeile 113*/
            return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
        } else {
            /**
             * return 404
             * Aktivierung Code stimmt nicht Überein
             *
             * body(jsonTester) später Löschen
             */
            //return ResponseEntity.status(HttpStatus.OK)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonTest);
        }

    }
}
