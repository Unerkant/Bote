package Bote.repository;

import Bote.model.Phone;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
   *    Den 3.01.2022
   */

public interface PhoneRepository extends CrudRepository<Phone, Integer> {

    /* Benutzt von PhoneController */
    List<Phone> findByToken(String token);
    Long deleteById(Long id);

    /* Benutzt von SettingController/@PostMapping(value = "/accountloschen") */
    String deleteByToken(String token);
}
