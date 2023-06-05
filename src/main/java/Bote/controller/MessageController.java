package Bote.controller;

import Bote.model.Freunde;
import Bote.model.Message;
import Bote.model.Usern;
import Bote.service.FreundeService;
import Bote.service.MessageService;
import Bote.service.CountEntryService;
import Bote.service.UserService;

import lombok.SneakyThrows;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


   /**
    * Created by Paul Richter on Mon 19/07/2021
    */

@Controller
public class MessageController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private CountEntryService countEntryService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private FreundeService freundeService;



   /**
    *   Message Ausgabe wird geleert, Rechte Seite...
    *   messenger.html Zeile: 189 ()switch schleife
    *   ..< div data-th-case="*" >..
    *
    *   Post Request abgesendet von messenger.js Zeile: 169
    *   Benutzt wird von messagecomponents.html Zeile: 36 + 39
    *   ..< a href="#" th:onClick="messageZuruck()">Zurück < /a >..
    *   ..< a href="#" th:onClick="messageSchliessen()" >Schließen < /a >..
    */
    @PostMapping(path = "/Leer")
    public String leer(){
        return "/messenger :: #MESSAGEFRAGMENT";
    }



   /**
    *   Meine Freunde Anzeigen, linke Teil von messenger.html
    *
    *   bei vorhandenem Cookie werden in H2: Datenbak Tabelle: FREUNDE nach
    *   freunden gesucht und in messenger.html ausgegeben(Linke Teil)
    *
    */
    private Usern           meineDaten;
    private List<Freunde>   meineFreunde;
    @GetMapping(value = {"/", "/messenger"})
    public String index(@CookieValue(value = "userid", required = false) String meineId, Model model)
    {
        meineDaten      = userService.meineDatenHolen(meineId);
        meineFreunde    = freundeService.freundeSuchenMitLetzterNachricht(String.valueOf(meineId));

        /* Freunden-Daten + meineId an messenger.html senden -> Freunde ausgeben(Linke Seite) Zeile: 67 + 71 */
        model.addAttribute("meinefreunde", meineFreunde);
        model.addAttribute("meineId", meineId);
        model.addAttribute("meinedaten", meineDaten);

        //System.out.println("MessageController, meine Freunde: " + meineFreunde);

        // wenn in Datenbank keine Daten vorhanden sind: return zum Registrieren
        return (meineDaten == null ? "/login/maillogin" : "/messenger");
    }



   /**
    *   chat mit Freund Starten
    *
    *   von angeklickten freund(Links) wird Chat Verlauf angezeigt (Rechts)
    *   javascript-function in messenger.js = $('.freund').click(function(e){}
    */
    private Usern           myDaten;
    private String          meinPseu;
    private String          meinMessageToken;
    private Usern           freundDaten;
    private String          freundToken;
    private String          freundMessageToken;
    private List<String>    alleFreundeMessageToken;
    private String          freundPseu;
    private List<Message>   gemeinsameMessage;
    private String          nameFragment;
    @SneakyThrows
    @PostMapping(path = "/fragmentmessages")
    public String fragmentMessages(@CookieValue(value = "userid", required = false) String meinecookie,
            HttpServletRequest request, Model model){


        // Fragment Name zugesendet per post(jQuery) von messenger.js Zeile:52
        // nameFragment: Anzeigen von Chat-Fragment  in messenger.html Zeile:145
        nameFragment = request.getParameter("nameFragment");

        freundToken = request.getParameter("freundeId");
        freundMessageToken = request.getParameter("freundMessageId");

        // finden in H2 das gleiche messageToken wie von meiner Chat-Freund
        alleFreundeMessageToken = freundeService.freundeSuchen(String.valueOf(meinecookie))
                .stream()
                .map(Freunde::getMessagetoken).collect(Collectors.toList());

        for (String token : alleFreundeMessageToken){
            if(token.equals(freundMessageToken)){
                meinMessageToken = token;
            }
        }

        myDaten = userService.meineDatenHolen(meinecookie);
        meinPseu = myDaten.getPseudonym();

        freundDaten = userService.meineDatenHolen(freundToken);
        freundPseu = freundDaten.getPseudonym();

        gemeinsameMessage = messageService.gemeisameMessage(freundMessageToken);

        //weitergeleitet an fragments/messagecomponents.html
        model.addAttribute("nameFragment", nameFragment);
        model.addAttribute("meinToken", meinecookie);
        model.addAttribute("meinPseudonym", meinPseu);
        model.addAttribute("meinMessageToken", meinMessageToken);
        model.addAttribute("freundToken", freundToken);
        model.addAttribute("freundPseu", freundPseu);
        model.addAttribute("freundMessageToken", freundMessageToken);
        model.addAttribute("gemeinsamemessage", gemeinsameMessage);

        /*logger.info("MessageController/PostMapping: "  + nameFragment );*/
        System.out.println("Message Controller, message Anzeige: " + gemeinsameMessage);

       return "/messenger :: #MESSAGEFRAGMENT";
    }



   /**
    *   Messsage Senden
    *   gestartet in messenger.js  stompClient.send("/app/messages" Zeile:156
    *   rückgabewert an messenger.js function connect().. Zeile:82
    */
    @MessageMapping("/messages")
    //@SendTo("/messages/receive")
    public void messageReceiving(Message message) throws Exception {

        // message count ins datenbank speichern
        int newCounterValue = countEntryService.incrementMessageCounter(message.getMeintoken());

        // message save
        messageService.saveNewMessage(message);
        //logger.info("Send to::  Freund token: " + message.getFreundetoken() + " /  Nachrichten insgesamt versendet:" + newCounterValue);
        simpMessagingTemplate.convertAndSend("/messages/receive/" + message.getFreundetoken(), message);

        //logger.info("Send to::  Mein token: " + message.getMeintoken() + " /  Nachrichten insgesamt versendet:" + newCounterValue);
        simpMessagingTemplate.convertAndSend("/messages/receive/" + message.getMeintoken(), message);
    }

}
