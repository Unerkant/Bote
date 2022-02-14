package Bote.service;

import Bote.model.User;
import Bote.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Paul Richter on Fri 20/08/2021
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /* Finde Alle Users */
    public List<User> findeAlle(){
        return userRepository.findAll();
    }

    /* functioniert 100% */
    public User findeUserToken(Long token){

        return userRepository.findByToken(token);
    }

    /* FUNCTIONIERT NICHT */
    public boolean mailRegistered(String user)
    {
        User savedUser = userRepository.findById(user);   //.orElse(null);

        return savedUser != null;
    }

    /* neue user ins H2 Datenbank speichern: func. 100% */
    public void saveNewUser(User user) {

        userRepository.save(user);
    }

    /* nach registrierte E-Mail suchen: func. 100% */
    public User sucheMail(String email) {

        return userRepository.findByEmail(email);
    }

    /**
     *   vorhandenen Telefonnummer suchen
     */
    public User sucheTelefon(String telefon){

        return userRepository.findByTelefon(telefon);
    }
}
