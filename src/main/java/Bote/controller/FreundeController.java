package Bote.controller;

import Bote.configuration.GlobalConfig;
import Bote.model.Freunde;
import Bote.model.User;
import Bote.service.FreundeService;
import Bote.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;


/**
    *  Den 1.10.2021
    */

@Controller
public class FreundeController {

    private String  myId;
    private User    myDaten;
    private List    alleUser;
    private String  freundTokenSave;
    private String  freundPseuSave;
    private String  meinTokenSave;
    private String  meinPseuSave;
    private String  registrierDatum;
    private String  unserMessageToken;

    public FreundeController()  throws IOException, ParseException { }
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;
    @Autowired
    private  FreundeService freundeService;

       /**
        *   Neuer Chat(Freunde) auflisten zum auswellen
        */
       @PostMapping("/neuerchat")
       public String neuerChat(HttpServletRequest request, Model model){

           myId     = request.getParameter("meinId");

           myDaten  = userService.findeUserToken(myId);
           alleUser = userService.findeAlle();
           model.addAttribute("myDaten", myDaten );
           model.addAttribute("allUser", alleUser);

           // für die javascript function 'neuerFreund(...)'in Fragment:neuerchat
           model.addAttribute("myToken", myDaten.getToken());
           model.addAttribute("myPseudonym", myDaten.getPseudonym());

           logger.info("Freunde Controller" + alleUser);
           return "messenger :: #NEUERCHAT";
       }

   /**
    *   Ausgewelte Freund(neuer Chat) speichern
    */
    @PostMapping("/neuerchatsave")
    public String neuerChatSave(HttpServletRequest request){

        registrierDatum     = GlobalConfig.deDatum();
        freundTokenSave     = request.getParameter("freundToken");
        freundPseuSave      = request.getParameter("freundPseu");
        meinTokenSave       = request.getParameter("meinToken");
        meinPseuSave        = request.getParameter("meinPseu");
        unserMessageToken   = GlobalConfig.IdentifikationToken();

        /* new Mein Chat save */
        Freunde newMeinChat = new Freunde();
        newMeinChat.setDatum(registrierDatum);
        newMeinChat.setFreundetoken(freundTokenSave);
        newMeinChat.setFreundpseudonym(freundPseuSave);
        newMeinChat.setMeinentoken(meinTokenSave);
        newMeinChat.setMeinpseudonym(meinPseuSave);
        newMeinChat.setMessagetoken(unserMessageToken);
        newMeinChat.setRole("ich");

        freundeService.saveMeinChat(newMeinChat);

        /* new Freund Chat save */
        Freunde newFreundChat = new Freunde();
        newFreundChat.setDatum(registrierDatum);
        newFreundChat.setFreundetoken(meinTokenSave);
        newFreundChat.setFreundpseudonym(meinPseuSave);
        newFreundChat.setMeinentoken(freundTokenSave);
        newFreundChat.setMeinpseudonym(freundPseuSave);
        newFreundChat.setMessagetoken(unserMessageToken);
        newFreundChat.setRole("freund");

        freundeService.saveFreundChat(newFreundChat);
        
        logger.info("Neuer Chat Save:" + unserMessageToken +"/"+freundTokenSave+"/"+freundPseuSave+"/"+meinTokenSave+"/"+meinPseuSave);
        return "redirect:/messenger";
   }



}
