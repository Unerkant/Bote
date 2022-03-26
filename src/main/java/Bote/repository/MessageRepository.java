package Bote.repository;

import Bote.model.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Paul Richter on Sat 24/07/2021
 */
public interface MessageRepository extends CrudRepository<Message, Long> {

    List<Message> findByMessagetoken(String messagetoken);

   /**
    *   benutzt von SettingController/@PostMapping(value = "/accountloschen")
    *   messagetoken ist einen array, jeder einzelnen satz von array wird
    *   einen nach den anderen abgearbeitet, z.b.s wen sind in array 10 messagenToken
    *   dann werden von allen 20 User(ein massgetoken sind für 2 user) alle
    *   message gelöscht...
    */
    String deleteByMessagetokenIn(List<String> messagetoken);
    
}
