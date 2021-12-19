package Bote.controller;

import Bote.configuration.GlobalConfig;
import Bote.model.Freunde;
import Bote.model.Message;
import Bote.model.User;
import Bote.service.FreundeService;
import Bote.service.MessageService;
import Bote.service.SettingEntryService;
import Bote.service.UserService;

import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Paul Richter on Mon 19/07/2021
 */

@Controller
public class MessageController {

    private List<Freunde>   meineFreunde;
    private User            findMyTokenInH2;

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserService userService;
    @Autowired
    private SettingEntryService settingEntryService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private FreundeService freundeService;

   /**
    *   Messenger HTML Starten
    *   wird ausgeführt nur bei vorhandenen COOKIE...[userid]
    *   unter die Adresse localhost:8080 oder localhost:8080/messenger
    *
    *   bei vorhandenen Cookie werden in H2:Datenbak Tabelle:FREUNDE nach
    *   freunden gesucht und in messenger.html ausgegeben(Linke Teil)
    *
    */
    @GetMapping(value = {"/", "/messenger"})
    public String index(@CookieValue(value = "userid", required = false) String meineId, Model model,
                        HttpServletRequest request, HttpServletResponse response)
    {
        /** prüfen in H2 nach vorhandene Cookie(userid)
         *  wenn Datenbank leer ist, Cookie Löschen
         *  zurück zum login
         */
        findMyTokenInH2 = userService.findeUserToken(meineId);
        if (findMyTokenInH2 == null){
            //meineId = null;
            GlobalConfig.deleteCookie(response);
        }

        //freundToken = request.getParameter("freunde");
        meineFreunde = freundeService.freundeSuchen(meineId);
        /* Freunden-Daten an mess.html senden -> Freunde ausgeben(Linke Seite) */
        model.addAttribute("meinefreunde", meineFreunde);
        model.addAttribute("meineId", meineId);

        logger.info("MessageController/GetMapping/ meine Cookie(token): " + meineId);
        return (meineId == null ? "/login/maillogin" : "/messenger");
    }


   /**
    *   chat mit Freund Starten
    *
    *   wird chat-verlauf angezeigt von angeclickten freund(Rechte Teil:message)
    *   gestartet wird in messenger.js = $('.freund').click(function(e){}
    */
    private User            meineDaten;
    private String          meinPseu;
    private String          meinMessageToken;
    private User            freundDaten;
    private String          freundToken;
    private String          freundMessageToken;
    private String          freundPseu;
    private List<Message>   gemeinsameMessage;

    @SneakyThrows
    @PostMapping(path = "/fragmentmessages")
    public String fragmentMessages(@CookieValue(value = "userid", required = false) String meinecookie,
            HttpServletRequest request, Model model){

        //zugesendet per post(jQuery) von messenger.js
        meinMessageToken = request.getParameter("freundMessageId");
        freundToken = request.getParameter("freundeId");
        freundMessageToken = request.getParameter("freundMessageId");

        meineDaten = userService.findeUserToken(meinecookie);
        meinPseu = meineDaten.getPseudonym();

        freundDaten = userService.findeUserToken(freundToken);
        freundPseu = freundDaten.getPseudonym();

        gemeinsameMessage = messageService.gemeisameMessage(freundMessageToken);

        //weitergeleitet an fragments/messagecomponents.html
        model.addAttribute("meinToken", meinecookie);
        model.addAttribute("meinPseudonym", meinPseu);
        model.addAttribute("freundToken", freundToken);
        model.addAttribute("freundPseu", freundPseu);
        model.addAttribute("meinMessageToken", meinMessageToken);
        model.addAttribute("freundMessageToken", freundMessageToken);
        model.addAttribute("gemeinsamemessage", gemeinsameMessage);

        logger.info("PostMapping: " + gemeinsameMessage);

       return "messenger :: #MESSAGEFRAGMENT";
    }


   /**
    *   Messsage Senden
    *   gestartet in messe.js  stompClient.send("/app/messages" Zeile:122
    *   rückgabewert an messenger.js function connect().. Zeile:87
    */

    @MessageMapping("/messages")
    @SendTo("/messages/receive")
    public Message messageReceiving(Message message) throws Exception {

        int newCounterValue = settingEntryService.incrementMessageCounter();
        logger.info("Nachrichten insgesamt versendet: " + newCounterValue);

        messageService.saveNewMessage(message);
        return message;
    }

}
