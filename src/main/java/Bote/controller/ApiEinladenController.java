package Bote.controller;

import Bote.configuration.GlobalConfig;
import Bote.model.Freunde;
import Bote.model.Usern;
import Bote.service.FreundeService;
import Bote.service.UserService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Den 10.12.2022
 */

@Controller
public class ApiEinladenController {

    @Autowired
    private UserService userService;
    @Autowired
    private FreundeService freundeService;

    /** SPÄTER LÖSCHEN ODER ÄNDERN
     *
     * nur für die Bekannten Einladen Anzeige
     *
     * Zugesendet von BoteFx/FreundeController/bekanntenEinladen Zeile: 320
     * der token wird nicht benutzt, als ballast zugesendet
     *
     * response: alle User in Datenbank finden und an BoteFX senden
     *
     * @param myToken
     * @return
     */
    @PostMapping(value = "alleUserApi")
    public @ResponseBody ResponseEntity<List<Usern>> alleUserSuchen(@RequestBody String myToken){

        /**
         *  für den Token zurzeit keine verwendung
         *  zugesendeten von BoteFX/FreundeController/private void bekanntenEinladen()
         *
         *  der Token werde zugesendet nur für den Request aufzubauen um zu User-Daten holen
         */
        JSONObject object = new JSONObject(myToken);
        String zugesendetToken = object.getString("sendeToken");

        // Alle user in Datenbank finden
        List allUser = userService.findeAlle();

        return ResponseEntity.status(HttpStatus.OK).body(allUser);
    }


    /**
     * zugesendet von BoteFX/einladenController/einladenRequest Zeile: ab 150
     * zugesendete parameter (3):
     * 1. sendeTelMail: eine E-Mail-Adresse oder Telefonnummer
     * 2. sendeKey: String 'mail' oder 'telefon', zu erkennen was ist zugesendet?
     * 3. Meiner Token(bei Bote: cookie)
     *
     * @param zugesandDaten
     * @return
     */
    @PostMapping(value = "/einladenApi")
    public @ResponseBody ResponseEntity<String> mailTelefonSuchen(@RequestBody String zugesandDaten){

        JSONObject obj = new JSONObject(zugesandDaten);
        String mailOderTelefon  = obj.getString("sendeTelMail");
        String key              = obj.getString("sendeKey");
        String myToken          = obj.getString("sendeToken");
        String output = null;
        Usern bekanntenDaten = null;
        String bekanntenToken = null;
        Usern meinerDaten = userService.findeUserToken(myToken);

        // Prüfen, ob schon Freund ist
        List allFreunde = freundeService.freundeSuchen(myToken)
                .stream()
                .map(Freunde::getFreundemail).collect(Collectors.toList());
        System.out.println("List: " + allFreunde);
        Object[] arr = allFreunde.toArray();
       // System.out.println("array: " + arr.toString());
        if (Arrays.stream(arr).anyMatch(s -> s.equals(mailOderTelefon))){
            output = "schonFreund";
            return ResponseEntity.status(HttpStatus.OK).body(output);
        }

        // mail/telefon suchen, prüfen auf eigenes mail/telefon, speichern in tabelle freunde
        if (key.equals("mail")){
            bekanntenDaten = userService.sucheMail(mailOderTelefon);
            if (bekanntenDaten != null){

                /**
                 * prüfen, ob es nicht eigene Daten(mail) sind.
                 * weans gleich Token, dann ist gleiche E-Mail,
                 * Erklärung: weil, mit der zugesendete mail wir holen alle Daten mit diesem mail ab
                 * dann ist das logisch da wird das gleiche mail sein... aber Token muss von bekannten sein...
                 */
                bekanntenToken = bekanntenDaten.getToken();
                if (myToken.equals(bekanntenToken)){
                    output = "eigenesMail";
                } else {
                    // Eintrag ins Datenbank
                    neuerChatSave(meinerDaten, bekanntenDaten);
                    output = "plusFreund";
                }
            }else{
                output = "noMail";
            }

        } else if (key.equals("telefon")) {
            bekanntenDaten = userService.sucheTelefon(mailOderTelefon);
            if (bekanntenDaten != null){

                /**
                 * prüfen, ob es nicht eigene Daten(telefon) sind.
                 * weans gleich Token, dann ist gleiche Telefon,
                 * Erklärung: weil, mit der zugesendete telefon wir holen alle Daten mit diesem telefonnummer ab
                 * dann ist das logisch da wird das gleiche telefonnummer sein...
                 * Fazit: aber Token muss von bekannten sein...
                 */
                bekanntenToken = bekanntenDaten.getToken();
                if (myToken.equals(bekanntenToken)){
                    output = "eigenesTelefon";
                } else {
                    // Eintrag ins Datenbank
                    neuerChatSave(meinerDaten, bekanntenDaten);
                    output = "plusFreund";
                }

            } else {
                output = "noTelefon";
            }

        }else {
            output = "noFound";
        }
        // response als string
        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    /**
     * Neuer Chat-Freund ins Datenbank speichern
     *
     * gesteuert von @PostMapping(value = "/einladenApi") hier oben Zeile 70
     *
     * @param meinData
     * @param freundData
     */
    private void neuerChatSave(Usern meinData, Usern freundData){

        String      werdeEingeladen     = "werdeeingeladen";
        String      wartenAufOk         = "wartenaufok";
        String unsereMessageToken = GlobalConfig.IdentifikationToken();

        // Daten von mir ins Freunde Tabelle speichern
        Freunde meinChatSave = new Freunde();
        meinChatSave.setDatum(meinData.getDatum());
        meinChatSave.setFreundebild(meinData.getBild());
        meinChatSave.setFreundemail(meinData.getEmail());
        meinChatSave.setFreundename(meinData.getName());
        meinChatSave.setFreundepseudonym(meinData.getPseudonym());
        meinChatSave.setFreundetelefon(meinData.getTelefon());
        meinChatSave.setFreundetoken(meinData.getToken());
        meinChatSave.setFreundevorname(meinData.getVorname());
        meinChatSave.setMeinentoken(freundData.getToken());
        meinChatSave.setMessagetoken(unsereMessageToken);
        meinChatSave.setRole(werdeEingeladen);

        freundeService.saveMeinChat(meinChatSave);

        // Daten von Eingeladenen ins Tabelle freund speichern
        Freunde freundChatSave = new Freunde();
        freundChatSave.setDatum(freundData.getDatum());
        freundChatSave.setFreundebild(freundData.getBild());
        freundChatSave.setFreundemail(freundData.getEmail());
        freundChatSave.setFreundename(freundData.getName());
        freundChatSave.setFreundepseudonym(freundData.getPseudonym());
        freundChatSave.setFreundetelefon(freundData.getTelefon());
        freundChatSave.setFreundetoken(freundData.getToken());
        freundChatSave.setFreundevorname(freundData.getVorname());
        freundChatSave.setMeinentoken(meinData.getToken());
        freundChatSave.setMessagetoken(unsereMessageToken);
        freundChatSave.setRole(wartenAufOk);

        freundeService.saveFreundChat(freundChatSave);

    }

    /**
     * Einladung prüfen
     *
     * 1. wenn wird true zugesendet:
     *      in der Tabelle Freunde -> spalte role mit dieser messageToken werden die einträge
     *      von werdeeingeladen & warteaufok gelöscht, Fazit Freundschafr angenommen
     * 2. wenn wird false zugesendet:
     *      Daten von mir und eingeladenen werden aus die Tabelle Freunde mit dieser
     *      messageToken gelöscht
     *
     * @param zustelData
     * @return
     */
    @PostMapping(value = "/einladungPrufenApi")
    public @ResponseBody ResponseEntity<Boolean> einladungAnnehmen(@RequestBody String zustelData){

        JSONObject object = new JSONObject(zustelData);
        String messageToken = object.getString("messToken");
        boolean erlaubnis    = Boolean.parseBoolean(object.getString("erlaubt"));
        if (erlaubnis){
            freundeService.roleUpdate("", messageToken);
            //Integer count = freundeService.roleUpdate("", messageToken);
            return ResponseEntity.status(HttpStatus.OK).body(erlaubnis);
        } else {
            freundeService.freundLoschen(messageToken);
            //List<Freunde> geloscht = freundeService.freundLoschen(messageToken);
            return ResponseEntity.status(HttpStatus.OK).body(erlaubnis);
        }
    }

}
