package Bote.repository;

import Bote.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
    *   Den 24.02.2022
    */


@Repository
public interface SettingRepository extends CrudRepository<User, Integer> {

    /* Vorname Update: */
    @Modifying
    @Transactional
    @Query(value = "UPDATE USER SET vorname = :vorname WHERE token = :token", nativeQuery=true)
    Integer updateVorname(String vorname, String token);

    /* Name Update */
    @Modifying
    @Transactional
    @Query(value = "UPDATE USER SET name = :name WHERE token = :token", nativeQuery=true)
    Integer updateName(String name, String token);
}
