package Bote.service;

import Bote.repository.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

   /**
    *  Den 24.02.2022
    */
@Service
public class SettingService {

    @Autowired
    private SettingRepository repository;

    /**
     *   Benutzt von settingController/@PostMapping(value = "bildupload")
     *   Benutzt von settingController/@PostMapping(value = "/profilbildloschen")
     *   Profil Bild Name Update & Profil Bild Löschen(bei Löschen profilbild wird leer zugesendet)
     */
    public Integer freundeBildUpdate(String name, String token){

        return repository.freundeUpdateBild(name, token);
    }

    /**
     *   Benutzt von settingController/@PostMapping(value = "bildupload")
     *   Benutzt von settingController/@PostMapping(value = "/profilbildloschen")
     *   Profil Bild Name Update & Profil Bild Löschen(bei Löschen profilbild wird leer zugesendet)
     */
    public Integer usernBildUpdate(String name, String token){

        return repository.usernUpdateBild(name, token);
    }


    /* Vorname Update */
    public Integer vornameUpdate(String vorname, String token){

        return repository.updateVorname(vorname, token);
    }

    /* Vorname Update */
    public Integer nameUpdate(String name, String token){

        return repository.updateName(name, token);
    }

    /* Mail Update */
    public Integer mailUpdate(String name, String token){

        return repository.updateMail(name, token);
    }

    /* Telefon Update */
    public Integer telefonUpdate(String name, String token){

        return repository.updateTelefon(name, token);
    }
}
