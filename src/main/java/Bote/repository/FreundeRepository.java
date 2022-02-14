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

    List<Freunde> findByMeinentoken(String meinentoken);
    List<Freunde> findByMessagetoken(String messagetoken);
    List<Freunde> deleteByMessagetoken(String messagetoken);

    /* Update: Löschen von 2x einträgen von Spalte role */
    @Modifying
    @Transactional
    @Query(value = "UPDATE FREUNDE SET role = :role WHERE messagetoken = :messagetoken", nativeQuery=true)
    Integer updateRole(String role, String messagetoken);
}
