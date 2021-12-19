package Bote.repository;

import Bote.model.Freunde;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
    *   Den 1.10.2021
    */

public interface FreundeRepository extends CrudRepository<Freunde, Integer> {

    List<Freunde> findByMeinentoken(String meinentoken);
    Freunde findByRole(String role);
}
