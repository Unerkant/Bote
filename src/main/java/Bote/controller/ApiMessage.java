package Bote.controller;

import Bote.model.Message;
import Bote.service.MessageService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
}
