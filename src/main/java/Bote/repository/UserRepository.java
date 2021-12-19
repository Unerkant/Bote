package Bote.repository;

import Bote.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
    * Created by Paul Richter on Fri 20/08/2021
    */

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User findById(String id);
    User findByToken(String token);
    User findByEmail(String email);
    User findByTelefon(String telefon);
    List<User> findAll();

}
