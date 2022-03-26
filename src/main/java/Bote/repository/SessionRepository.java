package Bote.repository;

import Bote.model.Session;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
    *   den 8.10.2021
    */

@Repository
public interface SessionRepository extends CrudRepository<Session, Integer> {

   /* nicht getestet */
   Session findByToken(String token);

   /**
    *   benutzt von SettingController/@PostMapping(value = "/accountloschen")
    *   aus der Datenbank: Tabelle Session alle einträge Löschen
    */
   String deleteByToken(String token);

   /**
    * benutzt von
    * nur datum Update in spalte 'letztenlogin'
    */
   @Modifying
   @Transactional
   @Query(value = "UPDATE SESSION SET letztenlogin = :login WHERE token = :token", nativeQuery=true)
   Integer updateLogin(String login, String token);

   /**
    * benutzt von SettingController/@PostMapping(value = "/accountabmelden")
    * nur datum Update in spalte 'letztenoutlog'
    */
   @Modifying
   @Transactional
   @Query(value = "UPDATE SESSION SET letztenoutlog = :outlog WHERE token = :token", nativeQuery=true)
   Integer updateOutlog(String outlog, String token);

}
