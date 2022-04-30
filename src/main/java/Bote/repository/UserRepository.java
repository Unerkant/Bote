package Bote.repository;

import Bote.model.Usern;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

    /**
    * Created by Paul Richter on Fri 20/08/2021
    */

@Repository
public interface UserRepository extends CrudRepository<Usern, Integer> {

    Usern findByToken(String token);
    Usern findByEmail(String email);
    Usern findByTelefon(String telefon);
    List<Usern> findAll();

   /**
    *   benutzt von SettingController/ @PostMapping(value = "/accountloschen")
    *   Account Löschen
    */
    String deleteByToken(String token);

}
