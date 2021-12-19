package Bote.repository;

import Bote.model.Session;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

   /**
    *   den 8.10.2021
    */

@Repository
public interface SessionRepository extends CrudRepository<Session, Integer> {

   Session findByToken(String token);

}
