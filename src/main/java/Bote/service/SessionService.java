package Bote.service;

import Bote.model.Session;
import Bote.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

   @Autowired
   private SessionRepository sessionRepository;

   /**
    *   nicht getestet
    */
    public Session findeId(String token){

       return sessionRepository.findByToken(token);
    }



   /**
    *   Registrierte User, Zeit der einloggen & ausloggen
    *   in die Tabelle SESSION speichern
    *
    *   ACHTUNG: zurzeit funktioniert nicht
    */

    public void saveLogDaten(Session logData){

        sessionRepository.save(logData);
    }

}
