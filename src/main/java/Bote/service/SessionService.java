package Bote.service;

import Bote.model.Session;
import Bote.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    *   bentzt von MailRegisterController und TelefonRegisterController
    *   Zeile 151 und 137
    *   registrierte User, Zeit der einloggen & ausloggen
    *   in die Tabelle SESSION speichern
    *
    *   ACHTUNG: zurzeit funktioniert nicht
    */
    public void saveLogDaten(Session logData){

        sessionRepository.save(logData);
    }
   /**
    *   benutzt von MailRegisterControlle Zeile: 154
    */
    public  Integer letzteloginUpdate(String datum, String token){
        return sessionRepository.updateLogin(datum, token);
    }

   /**
    *   benutzt von SettingController/@PostMapping(value = "/accountabmelden")
    *   Update: nur datum in Tabelle Session spalte: letztenoutlog
    */
    public Integer letztenoutlogUpdate(String name, String token){
        return sessionRepository.updateOutlog(name, token);
    }

    /**
    *   benutzt von SettingController/@PostMapping(value = "/accountloschen")
    *   aus der Datenbank: Tabelle Session alle einträge Löschen
    */
   @Transactional
    public String allUserSessionLoschen(String token){

       return sessionRepository.deleteByToken(token);
    }
}
