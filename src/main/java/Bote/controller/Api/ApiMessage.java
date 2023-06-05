package Bote.controller.Api;

import Bote.model.Message;
import Bote.service.MessageService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

/**
 * Den 23.12.2022
 */

@Controller
public class ApiMessage {

    @Autowired
    MessageService messageService;


    /**
     * Alle messages von mir & Freund holen, voraussetzung messageToken...
     *
     * Benutzt von BoteFX/MessageController Zeile: 145
     * public void setMessageToken(String mesagetoken)...
     *
     * @param sendTok
     * @return
     */
    @PostMapping(value = "/messageApi")
    public @ResponseBody ResponseEntity<List<Message>> apiMessage(@RequestBody String sendTok){

        JSONObject object = new JSONObject(sendTok);
        String messageToken = object.getString("messagesToken");
        List allMessage = messageService.gemeisameMessage(messageToken);

        //System.out.println("Response: " + allMessage);
        return ResponseEntity.status(HttpStatus.OK).body(allMessage);
    }



    /**
     * Message Löschen nach ID (als List<Long>)
     *
     * MessageController/messageLoschen die angeklickte message zum Löschen werden in einem
     * List-Array zugesendet, nur id: [205, 207, 202, 200]
     *
     * zugesendet von BoteFx/MessageController/messageLoschen Zeile: ca. 1000
     *
     * @param sendIds
     * @return
     */
    @PostMapping(value = "/messageLoschenApi")
    public @ResponseBody ResponseEntity<Integer> apiLoschenMessage(@RequestBody List<Long> sendIds){

        Integer loschCount =  messageService.listMessageLoschen(sendIds);

        return ResponseEntity.status(HttpStatus.OK).body(loschCount);
    }



    /**
     * Message Bearbeiten, BoteFX/MessageController
     *
     * @param sendMessage
     * @return
     */
    @PostMapping(value = "/messageUpdateApi")
    public @ResponseBody ResponseEntity<String> apiUpdateMessage(@RequestBody String sendMessage){

        return ResponseEntity.status(HttpStatus.OK).body(sendMessage);
        //return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.OK);
    }

}
