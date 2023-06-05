package Bote.controller.Api;

import Bote.model.Usern;
import Bote.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Den 24.04.2023
 */

@Controller
public class ApiUser {

    @Autowired
    private UserService userService;

    /**
     * Meine Daten aus dem Datenbank usern holen und zurück an BoteFX/MessageController
     *
     * output: Meine Daten:
     *      {"id":362,"token":"12042023204557","datum":"12-04-2023 20:45:57","bild":"","pseudonym":"MA",
     *      "name":"","vorname":"","email":"macbookpro@mail.com","telefon":"","role":"default","other":""}
     *
     * @param usersToken
     * @return
     */
    @PostMapping(value = "/userApi")
    public @ResponseBody ResponseEntity<Usern> apiUpdateMessage(@RequestBody String usersToken){

        JSONObject object = new JSONObject(usersToken);
        String meinesToken = object.getString("userToken");

        Usern myDaten = userService.meineDatenHolen(meinesToken);

        return ResponseEntity.status(HttpStatus.OK).body(myDaten);

    }
}
