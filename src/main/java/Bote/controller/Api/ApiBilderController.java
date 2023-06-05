package Bote.controller.Api;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;


/**
 * den 16.05.2023
 */

@Controller
public class ApiBilderController {



    /**
     *   Profil Bild aus dem Datei(profilbild) Laden und weiter leiten
     *
     *   zugesendet: zugesendet wird meinToken, weil alle Profil Bilder sind angelegt mit dem Namen von
     *               meinToken(z.b.s 07122022161046.png)
     *
     *   return:    wenn unter diesen token kein Bild gefunden wird, return null....
     *              Achtung: in Datenbank: tabelle messages gibst keine Bild-Adresse, das wegen wird meinToken benutzt
     *
     *   wird benutzt von:
     *      1. BoteFX/MessageCopntroller/messagesAusgeben()...Zeile:294
     *         Image cellProfilImg = new Image(ConfigService.FILE_HTTP+"profilbild/"+mess.getMeintoken()+".png", true);
     *
     *      2. BoteFx/FreundeCellController/ public void setProperties(Freunde frienden) Zeile: 152
     *         Image freundImg = new Image(ConfigService.FILE_HTTP+"profilbild/"+freundBild+".png", true);
     *
     */
    @GetMapping(value = "/profilbild/{imageName}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] profilBild(@PathVariable(value = "imageName", required = true) String bildName)
            throws IOException {

        File bildFile = new File("profilbild/" + bildName);

        if (bildFile.isFile()) {

            return IOUtils.toByteArray(bildFile.toURI());

        } else {

            return null;

        }

    }
}
