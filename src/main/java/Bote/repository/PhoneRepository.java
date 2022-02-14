package Bote.repository;

import Bote.model.Phone;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
   *    Den 3.01.2022
   */

public interface PhoneRepository extends CrudRepository<Phone, Integer> {

    List<Phone> findByToken(String token);
    Long deleteById(Long id);
}
