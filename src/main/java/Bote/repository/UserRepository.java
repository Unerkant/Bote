package Bote.repository;

import Bote.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

    /**
    * Created by Paul Richter on Fri 20/08/2021
    */

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User findByToken(Long token);
    User findByEmail(String email);
    User findByTelefon(String telefon);
    List<User> findAll();

   /**
    *   benutzt von SettingController/ @PostMapping(value = "/accountloschen")
    *   Account Löschen
    */
    String deleteByToken(Long token);


   /**
    *   Benutzt von settingController/@PostMapping(value = "bildupload")
    *   Benutzt von settingController/@PostMapping(value = "/profilbildloschen")
    *   Profil Bild Name Update & Profil Bild Löschen(bei Löschen profilbild wird leer zugesendet)
    */
    @Modifying
    @Transactional
    @Query(value = "UPDATE USER SET bild = :profilbild WHERE token = :token", nativeQuery=true)
    Integer updateBild(String profilbild, String token);

}
