package Bote.repository;

import Bote.model.Freunde;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

   /**
    *   Den 1.10.2021
    */

@Repository
public interface FreundeRepository extends CrudRepository<Freunde, Integer> {

   /**
    * Allgemeine Abfragen
    * deleteByMessagetoken wird benutzt:
    *   a. freundeService Zeile: 90
    *
    * @param meinentoken
    * @return
    */
    List<Freunde> findByMeinentoken(String meinentoken);
    List<Freunde> findByMessagetoken(String messagetoken);
    List<Freunde> deleteByMessagetoken(String messagetoken);


   /**
    *  benutzt von SettingController/@PostMapping(value = "/accountloschen")
    *  messagetoken ist einen array, von jeder-Satz-array werden
    *  aus der Tabelle gleich 2 einträge gelöscht(von mir und Freund)
    */
    String deleteByMessagetokenIn(List<String> messagetoken);


    /**
     *  Update: Löschen von 2x einträgen von Spalte role
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE FREUNDE SET role = :role WHERE messagetoken = :messagetoken", nativeQuery=true)
    Integer updateRole(String role, String messagetoken);

   }
