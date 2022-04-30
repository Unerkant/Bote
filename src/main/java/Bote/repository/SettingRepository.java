package Bote.repository;

import Bote.model.Usern;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
    *   Den 24.02.2022
    */


@Repository
public interface SettingRepository extends CrudRepository<Usern, Integer> {

    /**
     *   Benutzt von settingController/@PostMapping(value = "bildupload")
     *   Benutzt von settingController/@PostMapping(value = "/profilbildloschen")
     *   Profil Bild Name Update & Profil Bild Löschen(bei Löschen profilbild wird leer zugesendet)
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE FREUNDE SET freundebild = :profilbild WHERE freundetoken = :token", nativeQuery=true)
    Integer freundeUpdateBild(String profilbild, String token);

    /**
     *   Benutzt von settingController/@PostMapping(value = "bildupload")
     *   Benutzt von settingController/@PostMapping(value = "/profilbildloschen")
     *   Profil Bild Name Update & Profil Bild Löschen(bei Löschen profilbild wird leer zugesendet)
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE USERN SET bild = :profilbild WHERE token = :token", nativeQuery=true)
    Integer usernUpdateBild(String profilbild, String token);

    /* Vorname Update: */
    @Modifying
    @Transactional
    @Query(value = "UPDATE USERN SET vorname = :vorname WHERE token = :token", nativeQuery=true)
    Integer updateVorname(String vorname, String token);

    /* Name Update */
    @Modifying
    @Transactional
    @Query(value = "UPDATE USERN SET name = :name WHERE token = :token", nativeQuery=true)
    Integer updateName(String name, String token);

    /* Mail Update */
    @Modifying
    @Transactional
    @Query(value = "UPDATE USERN SET email = :mail WHERE token = :token", nativeQuery=true)
    Integer updateMail(String mail, String token);

    /* Telefon Update */
    @Modifying
    @Transactional
    @Query(value = "UPDATE USERN SET telefon = :telefon WHERE token = :token", nativeQuery=true)
    Integer updateTelefon(String telefon, String token);
}
