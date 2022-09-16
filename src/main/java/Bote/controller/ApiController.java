package Bote.controller;

import Bote.configuration.GlobalConfig;
import Bote.model.Session;
import Bote.model.Usern;
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
 * Den 24.08.2022
 */
@Controller
public class ApiController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;
    @Autowired
    private SessionService sessionService;

    private int     globalAktivierungscode;
    private String  newUserMail;
    private Integer codeZugesendet;
    private String  mailZugesendet;
    private String  neuerToken;
    private String  neuerDatum;
    private String  neuerPseudonym;
    private Usern   alterUser;
    private String  userResponse;

    /**
     * Aktivierung Code an E-Mail-Adresse senden
     *
     * An zugesendete E-Mail-Adresse(jsonDaten) wurde eine Aktivierung Code verschickt
     * Request wurde von BoteFx->MailLoginController->Methode: mailPrufen() zugesendet.
     * Output: 200 oder 404 an BoteFx->MailLoginController->Methode: mailPrufen() gesendet
     *
     * E-Mail Gesendet:         HttpStatus: 200
     * E-Mail nicht Gesendet:   HttpStatus: 404
     *
     * @return
     */
    @PostMapping(value = "/mailApi")
    public ResponseEntity<String> apiEmail(@RequestBody String mailZugesendet){

        /* Aktivierung Code holen & E-Mail mit code versenden */
        globalAktivierungscode = GlobalConfig.aktivierungCode();
        JSONObject ob = new JSONObject(mailZugesendet);
        newUserMail = (String) ob.get("neuUserMail");
        //String apiMailsenden = GlobalConfig.mailSenden(newUserMail, globalAktivierungscode);
        //String apiMailsenden = "ok";

      /*  if (apiMailsenden.equals("nomail")){
            System.out.println("Api Mail Controller IF: " + apiMailsenden);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            System.out.println("Api Mail Controller ELSE: " + apiMailsenden);
            return new ResponseEntity(HttpStatus.OK);
        }*/
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Aktivierung Code Prüfen
     *
     * Zugesendete Object von BoteFx->MailRegesterController.java->Methode: codePrufen
     *  1. code:    Aktivierungscode von BoteFx-> codePrufen Zeile: ca. 90
     *  2. mail:    E-Mail von zugesendeten Aktivierung Code, Quelle BoteFx
     *
     * @return
     */
    @PostMapping(value = "/codeApi")
    public @ResponseBody ResponseEntity<String> apiCode(@RequestBody String jsonZugesendet){

        /* Daten Sammeln */
        JSONObject object = new JSONObject(jsonZugesendet);
        codeZugesendet = object.getInt("code");
        mailZugesendet = object.getString("mail");
        neuerToken = GlobalConfig.IdentifikationToken();
        neuerDatum = GlobalConfig.deDatum();
        neuerPseudonym = mailZugesendet.substring(0, mailZugesendet.length() - mailZugesendet.length() + 2);
        neuerPseudonym = neuerPseudonym.toUpperCase(Locale.ROOT);

        /**
         *  Später Löschen
         *  Funktioniert nur bei Falsche Aktivierung Code eingabe.
         *
         *  Aktivierung Code zurück zu BoteFx senden wenn eine Falsche Code eingegeben ist,
         *  dient nur für Versuch Zwecken
         */
        String jsonTester = "{\"testerCode\":"+globalAktivierungscode+"}";
        /* Ende Später Löschen */

        /* Aktivierung Code Prüfen */
        if (codeZugesendet == globalAktivierungscode){

            /* nach E-Mail durchsuchen, ob vorhanden ist */
            alterUser = userService.sucheMail(mailZugesendet);
            if (alterUser == null){
                Usern neuerUser = new Usern();
                /* Neuer User Registrieren */
                neuerUser.setBild("");
                neuerUser.setDatum(neuerDatum);
                neuerUser.setEmail(mailZugesendet);
                neuerUser.setName("");
                neuerUser.setOther("");
                neuerUser.setPseudonym(neuerPseudonym);
                neuerUser.setRole("default");
                neuerUser.setTelefon("");
                neuerUser.setToken(neuerToken);
                neuerUser.setVorname("");
                userService.saveNewUser(neuerUser);

                /* neuer User Session anlegen  */
                Session sessionDaten = new Session();
                sessionDaten.setDatum(neuerDatum);
                sessionDaten.setLetztenlogin(neuerDatum);
                sessionDaten.setLetztenoutlog("");
                sessionDaten.setOther("");
                sessionDaten.setToken(neuerToken);
                sessionService.saveLogDaten(sessionDaten);

            } else {
                /* Datum Update: Tabelle Session, spalte: letztenlogin */
                sessionService.letzteloginUpdate(neuerDatum, String.valueOf(alterUser.getToken()));
            }

            /**
             *  Alten User Daten zurück an:
             *  BoteFx -> MailRegisterController.java -> public void codePrufen(ActionEvent event){}
             */
            userResponse =  "{\"bildOutput\":\"" +   ( alterUser != null ? alterUser.getBild()       : "" )+"\", " +
                            "\"datumOutput\":\"" +   ( alterUser != null ? alterUser.getDatum()      : neuerDatum )+"\", " +
                            "\"mailOutput\":\"" +    ( alterUser != null ? alterUser.getEmail()      : mailZugesendet )+ "\", " +
                            "\"nameOutput\":\"" +    ( alterUser != null ? alterUser.getName()       : "" )+ "\", " +
                            "\"otherOutput\":\"" +   ( alterUser != null ? alterUser.getOther()      : "" )+ "\", " +
                            "\"pseudonymOutput\":\""+( alterUser != null ? alterUser.getPseudonym()  : neuerPseudonym )+ "\", " +
                            "\"roleOutput\":\"" +    ( alterUser != null ? alterUser.getRole()       : "default" )+ "\", " +
                            "\"telefonOutput\":\"" + ( alterUser != null ? alterUser.getTelefon()    : "" )+"\", " +
                            "\"tokenOutput\":\""+    ( alterUser != null ? alterUser.getToken()      : neuerToken )+"\", " +
                            "\"vornameOutput\":\""+  ( alterUser != null ? alterUser.getVorname()    : "" )+"\" }";

            System.out.println("IF User Daten(response): " + userResponse );
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        } else {
            System.out.println("ELSE HttpStatus: " + jsonTester  );
            /**
             * return 404
             * Aktivierung Code stimmt nicht Überein
             */
            //return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonTester);
        }
    }
}
