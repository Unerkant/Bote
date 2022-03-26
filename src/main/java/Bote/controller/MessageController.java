package Bote.controller;

import Bote.model.Freunde;
import Bote.model.Message;
import Bote.model.User;
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
    *   Messenger HTML Starten
    *   wird ausgeführt nur bei vorhandenen Daten in Datenbank: Tabelle user
    *   unter die Adresse localhost:8080 oder localhost:8080/messenger
    *
    *   bei vorhandenen Cookie werden in H2:Datenbak Tabelle:FREUNDE nach
    *   freunden gesucht und in messenger.html ausgegeben(Linke Teil)
    *
    */
    private User            meineDaten;
    private List<Freunde>   meineFreunde;

    @GetMapping(value = {"/", "/messenger"})
    public String index(@CookieValue(value = "userid", required = false) Long meineId, Model model)
    {
        meineDaten      = userService.findeUserToken(meineId);
        meineFreunde    = freundeService.freundeSuchen(String.valueOf(meineId));

        /* Freunden-Daten + meineId an messenger.html senden -> Freunde ausgeben(Linke Seite) Zeile: 67 + 71 */
        model.addAttribute("meinefreunde", meineFreunde);
        model.addAttribute("meineId", meineId);
        model.addAttribute("meinedaten", meineDaten);

        logger.info("MessageController/GetMapping / index: " + meineFreunde);
        // wenn in Datenbank keine Daten vorhanden sind: return zum Registrieren
        return (meineDaten == null ? "/login/maillogin" : "/messenger");
    }


   /**
    *   chat mit Freund Starten
    *
    *   von angeklickten freund(Links) wird Chat Verlauf angezeigt (Rechts)
    *   javascript-function in messenger.js = $('.freund').click(function(e){}
    */
    private User            myDaten;
    private String          meinPseu;
    private String          meinMessageToken;
    private User            freundDaten;
    private String          freundToken;
    private String          freundMessageToken;
    private List<String>    alleFreundeMessageToken;
    private String          freundPseu;
    private List<Message>   gemeinsameMessage;
    private String          nameFragment;

    @SneakyThrows
    @PostMapping(path = "/fragmentmessages")
    public String fragmentMessages(@CookieValue(value = "userid", required = false) Long meinecookie,
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

        myDaten = userService.findeUserToken(meinecookie);
        meinPseu = myDaten.getPseudonym();

        freundDaten = userService.findeUserToken(Long.valueOf(freundToken));
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

        logger.info("MessageController/PostMapping: "  + nameFragment );

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

        int newCounterValue = countEntryService.incrementMessageCounter();
        logger.info("Nachrichten insgesamt versendet: " + newCounterValue);
        logger.info("MessageController/SendTo: " + message);

        messageService.saveNewMessage(message);

        logger.info("Send to: " + "/messages/receive/" + message.getMessagetoken());
        simpMessagingTemplate.convertAndSend("/messages/receive/" + message.getMessagetoken(), message);
    }

}
