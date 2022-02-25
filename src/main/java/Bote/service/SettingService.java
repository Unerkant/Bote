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

    /* Vorname Update */
    public Integer vornameUpdate(String vorname, String token){
        return repository.updateVorname(vorname, token);
    }

    /* Vorname Update */
    public Integer nameUpdate(String name, String token){
        return repository.updateName(name, token);
    }
}
