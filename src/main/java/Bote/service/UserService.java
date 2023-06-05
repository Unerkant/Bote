package Bote.service;

import Bote.model.Usern;
import Bote.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Created by Paul Richter on Fri 20/08/2021
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    /**
     * Alle User abruffen
     * @return
     */
    public List<Usern> findeAlle(){

        return userRepository.findAll();
    }


    /**
     * Meine Daten Holen
     * output: Meine Daten: {"id":362,"token":"12042023204557","datum":"12-04-2023 20:45:57","bild":"",
     * "pseudonym":"MA","name":"","vorname":"","email":"macbookpro@mail.com","telefon":"","role":"default","other":""}
     * @param token
     * @return
     */
    public Usern meineDatenHolen(String token){

        return userRepository.findByToken(token);
    }


    /**
     * Neuer User speichern
     * @param user
     */
    public void saveNewUser(Usern user) {

        userRepository.save(user);
    }


    /**
     * Nach regestrierten E-Mail suchen
     *
     * @param email
     * @return
     */
    public Usern sucheMail(String email) {

        return userRepository.findByEmail(email);
    }


   /**
    *   nach vorhandenen Telefonnummer suchen
    */
    public Usern sucheTelefon(String telefon){

        return userRepository.findByTelefon(telefon);
    }


   /**
    *   Benutzt von SettingController/@PostMapping(value = "/accountloschen")
    *   Account Löschen
    * @param token
    */
    @Transactional
    public String userLoschen(String token){

        return userRepository.deleteByToken(token);
    }

}
