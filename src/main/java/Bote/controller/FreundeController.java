package Bote.controller;

import Bote.configuration.GlobalConfig;
import Bote.model.Freunde;
import Bote.model.User;
import Bote.service.FreundeService;
import Bote.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;


   /**
    *  Den 1.10.2021
    */

@Controller
public class FreundeController {

    private User    meineDaten;
    public FreundeController()  throws IOException, ParseException { }
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FreundeService freundeService;
    @Autowired
    private UserService userService;

   /**
    *   NUR DIE SUCHFUNCTION EINBLENDEN MIT HEAD(Meine Daten)
    *
    *   Neuer Chat(Freunde) Einladen: anzeige in messenger.html (Rechte Seite:)
    *   data-th-fragment="neuerchatfragment" in messagecomponets.html Zeile: 158
    *
    *   'nameFragment' zugesendet von freunde.js Zeile: 39, function neuerChat(){...}
    */
    @PostMapping(value = "/neuerchat")
    public String neuerchat(@CookieValue(value = "userid", required = false) Long neuerChatCookie,
                            @RequestParam(value = "namefragment", required = false) String nameFragment,
                            Model model){
        /* myDaten: anzeige von head Fragments */
        meineDaten = userService.findeUserToken(neuerChatCookie);
        model.addAttribute("myDaten", meineDaten);

        /* nameFragment: Name von anzege-Fragments, messenger.html Zeile:154 */
        model.addAttribute("nameFragment", nameFragment);

        /* NUR FÜR INFORMATION: alle User anzeigen */
        List alleUser = userService.findeAlle();
        model.addAttribute("alleuser", alleUser);


        logger.info("Freunde-Controller: " + alleUser);
        return (meineDaten == null ? "/login/maillogin" : "/messenger :: #MESSAGEFRAGMENT");
    }


   /**
    *   Prüfen nach Meine Telefon/E-Mail und Freunde Telefon/E-Mail
    *
    *   Neuer Chat anlegen ( nach bekanten e-mail oder telefon suchen)
    *   Alle Daten an messagecomponets.html/neuerchatfragment schicken Zeile: 150
    */

    private User            myDaten;
    private List<String>    alleFreundeEmail;
    private List<String>    alleFreundeTelefon;
    private String          myTelefon;
    private String          myMail;
    private String          fehlerAusgabe;
    private User            datenGefunden;

    @PostMapping(value = "/bekantensuchen")
    public String bekantenSuchen(@CookieValue(value = "userid", required = false) Long bekantenCookie,
                                 @RequestParam(value = "mailodertel", required = false) String mailOderTel,
                                 @RequestParam(value = "chatfragment", required = false) String fragmentName,
                                 @RequestParam(value = "zahl", required = false) String istZahl,
                                 Model model){

/*  1.  myDaten: Meine Daten, für die Anzeige von head Fragments */
        myDaten = userService.findeUserToken(bekantenCookie);
        model.addAttribute("myDaten", myDaten);

/*  2.  Alle Telefonnummer oder E-Mail von User, mich und Freuden holen  */

        alleFreundeTelefon = freundeService.freundeSuchen(String.valueOf(bekantenCookie))
                .stream()
                .map(Freunde::getFreundetelefon)
                .collect(Collectors.toList());

        alleFreundeEmail = freundeService.freundeSuchen(String.valueOf(bekantenCookie))
                .stream()
                .map(Freunde::getFreundemail)
                .collect(Collectors.toList());

        myTelefon = myDaten.getTelefon();
        myMail = myDaten.getEmail();
        fehlerAusgabe = null;
/*  3.  auf eigenes Telefon oder E-Mail Prüfen */
      if (istZahl.equals("ZAHL")){
          if (mailOderTel.equals(myTelefon)){
              fehlerAusgabe = "eingenesTelefon";
          }else{
              // nach Freunden Telefon suchen
              loop1:
              for (String freundTelefon : alleFreundeTelefon){
                  if (freundTelefon != null && freundTelefon.equals(mailOderTel)){
                      fehlerAusgabe = "freundTelefon";
                      break loop1;
                  }
              }
          }
      }else{
          if (mailOderTel.equals(myMail)){
              fehlerAusgabe = "eigenesMail";
          }else{
              // nach Freunde E-Mail suchen
              loop2:
              for (String freundMail : alleFreundeEmail){
                  if (freundMail != null && freundMail.equals(mailOderTel)) {
                      fehlerAusgabe = "freundMail";
                      break loop2;
                  }
              }
          }
      }

/*  4. Suchen in Datenbank nach vorhandenen Telefonnummer oder E-Mail */
        if (fehlerAusgabe == null){
            if (istZahl.equals("ZAHL")) {
                datenGefunden = userService.sucheTelefon(mailOderTel);
                if (datenGefunden == null){
                    fehlerAusgabe = "noDaten";
                }

            }else{
                datenGefunden = userService.sucheMail(mailOderTel);
                if (datenGefunden == null){
                    fehlerAusgabe = "noDaten";
                }

            }
        }

        // Ausgabe in messagecomponents.ntml Zeile: 216 (div#NEUERCHATGEFUNDEN)
        model.addAttribute("nameFragment", fragmentName);
        model.addAttribute("fehlerAusgaben", fehlerAusgabe);
        model.addAttribute("datenGefunden", datenGefunden);
        model.addAttribute("suchOption", mailOderTel);


        /* NUR FÜR INFORMATION: alle User anzeigen */
        List alleUser = userService.findeAlle();
        model.addAttribute("alleuser", alleUser);

        logger.info("Bekanten Suchen: " + fehlerAusgabe+"/"+mailOderTel+"/"+myMail+"/"+datenGefunden);
        return ("/messenger :: #MESSAGEFRAGMENT");
    }


   /**
    *   Ausgewelte Freund(neuer Chat) speichern
    *
    */
    private String      unsereRegistrierDatum;
    private String      unsereMessageToken;

    private User        meinereDaten;
    private Long        meinereTokenSave;
    private String      meinereBildSave;
    private String      meinerePseuSave;
    private String      meinereNameSave;
    private String      meinereVornameSave;
    private String      meinereMailSave;
    private String      meinereTelSave;

    private User        bekanteDaten;
    private Long        bekanteTokenSave;
    private String      bekanteBildSave;
    private String      bekantePseuSave;
    private String      bekanteNameSave;
    private String      bekanteVornameSave;
    private String      bekanteMailSave;
    private String      bekanteTelSave;

    private String      werdeEingeladen     = "werdeeingeladen";
    private String      wartenAufOk         = "wartenaufok";

    private List<Freunde>   meinefreunde;

    @PostMapping(value = "/bekantensave")
    public String bekantenSave(@CookieValue(value = "userid", required = false) Long mySaveCookie,
                               @RequestParam(value = "bekantentoken", required = false) Long bekantenToken,
                               HttpServletRequest request, Model model){

        unsereRegistrierDatum   = GlobalConfig.deDatum();
        unsereMessageToken      = GlobalConfig.IdentifikationToken();

        meinereDaten        = userService.findeUserToken(mySaveCookie);
        meinereTokenSave    = meinereDaten.getToken();
        meinereBildSave     = meinereDaten.getBild();
        meinerePseuSave     = meinereDaten.getPseudonym();
        meinereNameSave     = meinereDaten.getName();
        meinereVornameSave  = meinereDaten.getVorname();
        meinereMailSave     = meinereDaten.getEmail();
        meinereTelSave      = meinereDaten.getTelefon();

        bekanteDaten        = userService.findeUserToken(bekantenToken);
        bekanteTokenSave    = bekanteDaten.getToken();
        bekanteBildSave     = bekanteDaten.getBild();
        bekantePseuSave     = bekanteDaten.getPseudonym();
        bekanteNameSave     = bekanteDaten.getName();
        bekanteVornameSave  = bekanteDaten.getVorname();
        bekanteMailSave     = bekanteDaten.getEmail();
        bekanteTelSave      = bekanteDaten.getTelefon();

       /** Speichern meine Daten in Datenbank Tabelle Freunde
        *
        * in Datenbank Tabelle: 'Freunde' werden Daten von mir und von
        * eingeladenen Freund gespeichert.
        * unter die Spalte 'meinenToken' werden token von Freund gespeichert
        * rest die daten von mir, da kann er meine Daten mit seinem Token
        * bei sich anzeigen lassen,
        * das Gleiche gilt für Freunde Daten speicherung, unter 'meinenToken'
        * wird meine Token angelegt und Rest Daten von Freund
        */
        Freunde newMeinChat = new Freunde();
        newMeinChat.setDatum(unsereRegistrierDatum);
        newMeinChat.setMeinentoken(String.valueOf(bekantenToken));
        newMeinChat.setMessagetoken(unsereMessageToken);
        newMeinChat.setFreundetoken(String.valueOf(meinereTokenSave));
        newMeinChat.setFreundebild(meinereBildSave);
        newMeinChat.setFreundepseudonym(meinerePseuSave);
        newMeinChat.setFreundename(meinereNameSave);
        newMeinChat.setFreundevorname(meinereVornameSave);
        newMeinChat.setFreundemail(meinereMailSave);
        newMeinChat.setFreundetelefon(meinereTelSave);
        newMeinChat.setRole(werdeEingeladen);

        freundeService.saveMeinChat(newMeinChat);

        /* speichern Freund Daten in Datenbank Tabelle Freunde */
        Freunde newFreundChat = new Freunde();
        newFreundChat.setDatum(unsereRegistrierDatum);
        newFreundChat.setMeinentoken(String.valueOf(meinereTokenSave));
        newFreundChat.setMessagetoken(unsereMessageToken);
        newFreundChat.setFreundetoken(String.valueOf(bekantenToken));
        newFreundChat.setFreundebild(bekanteBildSave);
        newFreundChat.setFreundepseudonym(bekantePseuSave);
        newFreundChat.setFreundename(bekanteNameSave);
        newFreundChat.setFreundevorname(bekanteVornameSave);
        newFreundChat.setFreundemail(bekanteMailSave);
        newFreundChat.setFreundetelefon(bekanteTelSave);
        newFreundChat.setRole(wartenAufOk);

        freundeService.saveFreundChat(newFreundChat);

        /* Daten an bekanteneinladung.html senden Fragment: werdeeingeladen */
        meinefreunde = freundeService.freundeSuchen(String.valueOf(mySaveCookie));
        model.addAttribute("meinefreunde", meinefreunde);
        model.addAttribute("unsereMessageToken", unsereMessageToken);

        logger.info("Neuer Chat Save:" + unsereMessageToken +"/"+ meinefreunde);
        /* Return an javascript (freunde.js) function bekantenSave(bekantenToken) Zeile:85 */
        return ("/messenger :: #NEUERCHAT");
   }


   /**
    *   Einladung genehmigung
    *   Datenbank: Tabelle Freunde Spalte: role, werden 2x
    *   einträge (werdeEingeladen/wartenAufOk) gelöscht von Freunde & mich WHERE messageToken
    */
   @PostMapping(value = "/einladungUpdate")
   public String einladungUpdate(@CookieValue(value = "userid", required = false) String einladungCookie,
                                 @RequestParam(value = "messageToken", required = false) String messageToken){

       Integer count;
       count = freundeService.roleUpdate("", messageToken);


       logger.info("PostMapping Freund Controller Update: "+ count);
       /* #BE Symbol ID, zu finden in bekanteneinladung.html Zeile:18 */
       return ("/messenger :: #BE");
   }


  /**
   *   Freund Löschen(Chat Löschen)
   *   Datenbank Tabelle: Freunde, wird gelöscht 2 einträge
   *   WHERE messagetoken
   */
   @PostMapping(value = "/freundedelete")
   public String freundLoschen(@CookieValue(value = "userid", required = false) String freundeDeleteCookie,
                               @RequestParam(value = "messToken", required = false) String messToken){

       freundeService.freundLoschen(messToken);
       logger.info("PostMapping Freund Loschen: "  + messToken );
       return ("/messenger :: #OK"+messToken);
   }

}
