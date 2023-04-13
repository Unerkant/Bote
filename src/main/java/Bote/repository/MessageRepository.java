package Bote.repository;

import Bote.model.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Created by Paul Richter on Sat 24/07/2021
 */

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

    List<Message> findByMessagetoken(String messagetoken);


   /**
    * von gelöschte Freund alle Message Löschen
    * benutzt von MessageService Zeile: 50
    * @param messagetoken
    * @return
    */
    List<Message> deleteByMessagetoken(String messagetoken);



   /**
    *   Message Loschen nach messageToken
    *
    *   benutzt von SettingController/@PostMapping(value = "/accountloschen")
    *   messagetoken ist einen array, jeder einzelnen satz von array wird
    *   einen nach den anderen abgearbeitet, z.b.s wen sind in array 10 messagenToken
    *   dann werden von allen 20 User(ein massgetoken sind für 2 user) alle
    *   message gelöscht...
    */
    String deleteByMessagetokenIn(List<String> messagetoken);



    /**
     *  benutzt von messageService, letzte message suchen und an Freunde array anhängen
     *  Zeile: 46
     *  erstelt am 26.11.2022, für den BoteFX/FreundeCellController Zeile: ab 160
     * @param messagetoken
     * @return
     */
    Message findFirstByMessagetokenOrderByIdDesc(String messagetoken);



    /**
     * Daten nach id Löschen, als List Array
     *
     * Quelle: BoteFX/MesssageController / private void messageLoschen(List<Long> mesageIds, List<StackPane> paneIds)
     * ... da sind die nachrichten was werden bei 'bearbeiten' ausgewählt(click auf drei Punkte, oben Rechts)
     * zugesendet werden nur id von jeder nachricht, als ListArray...
     * so sieht List<Long> aus [205, 207, 202, 200]
     *
     * @param id
     */
     void deleteAllById(Iterable<? extends Long> id);

}
