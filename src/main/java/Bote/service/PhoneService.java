package Bote.service;

import Bote.model.Phone;
import Bote.model.User;
import Bote.repository.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

   /**
    *   Den 3.01.2022
    */

@Service
public class PhoneService {

    @Autowired
    private PhoneRepository phoneRepository;

   /**
    * benutzt von PhoneController/@GetMapping(value = "/phone")
    * Alle Telefonate Ausgeben
    */
    public List<Phone> telefonatAusgeben(String token){
        List<Phone> telefonat = new ArrayList<>();
        phoneRepository.findByToken(token).forEach(telefonat::add);
        return telefonat;
    }


   /**
    * benutzt von PhoneController/@PostMapping(value = "/anrufdelete")
    * einzelnen Telefonat Löschen
    */
    @Transactional
    public void telefonatLoschen(Long id){

        phoneRepository.deleteById(id);
    }

   /**
    *  benutzt von SettingController/@PostMapping(value = "/accountloschen")
    *  100%, Alle Telefonate von einem User Löschen, nach token spalte
    */
    @Transactional
    public String allUserTelfonatLoschen(String token){

       return phoneRepository.deleteByToken(token);
    }

}
