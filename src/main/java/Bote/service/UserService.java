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

    /* Finde Alle Users */
    public List<Usern> findeAlle(){

        return userRepository.findAll();
    }

    /* functioniert 100% */
    public Usern findeUserToken(String token){

        return userRepository.findByToken(token);
    }

    /* neue user ins H2 Datenbank speichern: func. 100% */
    public void saveNewUser(Usern user) {

        userRepository.save(user);
    }

    /* nach registrierte E-Mail suchen: func. 100% */
    public Usern sucheMail(String email) {

        return userRepository.findByEmail(email);
    }

   /**
    *   vorhandenen Telefonnummer suchen
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
