package Bote.service;

import Bote.model.Freunde;
import Bote.model.Message;
import Bote.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Created by Paul Richter on Sat 24/07/2021
 */
@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;


    public void saveNewMessage(Message message) {
        messageRepository.save(message);
    }



   /**
    *   Messages einen Chat Laden
    *   gesucht wird nach message token... fun. 100%
    */
    public List<Message> gemeisameMessage(String token){

        return messageRepository.findByMessagetoken(token);
    }



   /**
    *   MESSAGE LÖSCHEN (bei accound löschen)
    *
    *   Alle vorhandene Nachrichten von zugesendete tokenMessage
    *   benutzt vo SettingController/@PostMapping(value = "/accountloschen")
    */
    @Transactional
    public String allMessagesLoschen(List<String> messagetoken) {

      return messageRepository.deleteByMessagetokenIn(messagetoken);
    }



    /**
     * Alle Nachrichten von gelöschten Freund Löschen
     * benutzt: ApiFreundeController Zeile: 81,
     * für den BoteFX/FreundeCellController/freundRemove() Zeile 275
     *
     * @param freundmessageloschen
     * @return
     */
    @Transactional
    public List<Message> freundMessageLoschen(String freundmessageloschen){
       return messageRepository.deleteByMessagetoken(freundmessageloschen);
    }



    /**
     * Diese Methode ist nicht performant, aber einfach
     * bei größeren Datenmengen sollte diese Funktion nochmal optimiert werden
     *
     * erstellt 26.11.2022, für die BoteFX, bei Freunde letzte Nachricht anzeigen
     * BoteFx/FreundeCellController Zeile: ab 150
     *
     * @param freunde
     */
    public void setzeLetzteNachrichten(List<Freunde> freunde) {
        for (Freunde freund: freunde) {
            Message letzteNachricht = messageRepository.findFirstByMessagetokenOrderByIdDesc(freund.getMessagetoken());
            if (letzteNachricht != null) {
                freund.setLetzteNachricht(letzteNachricht.getText());
                freund.setDatumLetzteNachricht(letzteNachricht.getDatum());
            }
        }
    }



    /**
     * Daten nach ID Löschen
     *
     * Erstellt für BoteFx/MessageController/messageLoschen(..
     * so sieht List<Long> aus [205, 207, 202, 200]
     *
     * return an BoteFX/MessageController/messageLoschen(...als response Zeile: ab 1000
     *
     * @param messageId
     * @return
     */
    public Integer listMessageLoschen(List<Long> messageId){
       messageRepository.deleteAllById(messageId);
       return messageId.size();
    }

}
