package Bote.controller;

import Bote.model.Freunde;
import Bote.model.Message;
import Bote.service.FreundeService;
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
 * Den 31.10.2022
 */

@Controller
public class ApiFreundeController {
    @Autowired
    private FreundeService freundeService;
    @Autowired
    private MessageService messageService;


    /**
     * Alle meine Freunde suchen & mit request an BoteFX FreundeController/freundeLaden()
     * PLUS: an der Freunde-array(myFreunde) wird die Letzte message-nachricht + Datum angehängt
     * kurze Beschreibung:
     * in den FreundeService Zeile: 39, werden 2 Methoden von FreundeService & MessageService
     * Daten abgerufen und in einem array (myFreunde) zusammengelegt, schliesslich an der
     * BoteFx/FreundeController als response gesendet, Zeile: 143
     *
     * response status zurücksenden: 200/500 + array
     * array wenn leer ist = []
     * return response.body();
     * z.b.s angekommen an BoteFX
     * Response: [{"id":5,"datum":"21-08-2022 15:43:22",...,"role":""},
     *            {"id":64,"datum":"06-11-2022 18:18:29", ...,"role":""},
     *            {"id":65,"meinentoken":"21082022154002",...,"role":""}]
     *
     * @param sendToken
     * @return
     */
    @PostMapping(value = "/freundeApi")
    public @ResponseBody ResponseEntity<List<Freunde>> apiFreunde(@RequestBody String sendToken){

        /* Alle meine Freunde suchen/holen */
        JSONObject object = new JSONObject(sendToken);
        String tokenZugesendet = object.getString("sendToken");
        List<Freunde> myFreunde = freundeService.freundeSuchenMitLetzterNachricht(tokenZugesendet);

        return ResponseEntity.status(HttpStatus.OK).body(myFreunde);
    }


    /**
     *  EINEN FREUND LÖSCHEN
     *
     *  für die Löschung wird den massageToken benutzt, MySql Tabelle 'freunde'
     *  gelöscht werden gleich 2 einträge, meinen und von Freund
     *  +
     *  tabelle 'messages' alle unsere gemeinsame messages(gleiche message token)
     *
     *  messageToken wird von dem BoteFX/FreundeCellController Methode
     *  freundRemove(), Zeile: 260, zugesendet als json array
     *
     *  response:
     *  1. bei nicht vorhandene messageToken: 'geloschteFreund.size()', ausgabe: 0
     *  2. bei gelöschten Freund: 'geloschteFreund.size()',     ausgabe: 2
     *  3. bei gelöschten Freund: 'geloschteFreund', ausgabe: als array mit Daten von mir & Freund
     *     [Freunde{id='113', datum='04-12-2022 14:59:17',..}, Freunde{id='114', datum='04-...}]
     *
     * @param sendMessageToken
     * @return
     */
    @PostMapping(value = "/freundeRemoveApi")
    public ResponseEntity apiFreundeRemove(@RequestBody String sendMessageToken){

        JSONObject ob = new JSONObject(sendMessageToken);
        String zugesandtMessageToken = (String) ob.get("sendmessagetoken");
        // Delete Freund + messages
        List<Freunde> geloschteFreund = freundeService.freundLoschen(zugesandtMessageToken);
        List<Message> geloschteNachrichten = messageService.freundMessageLoschen(zugesandtMessageToken);

        //System.out.println("Api Controller: " + geloschteFreund +"/"+ geloschteNachrichten.stream().count());
        return ResponseEntity.status(HttpStatus.OK).body(geloschteFreund.size());
    }

}
