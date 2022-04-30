package Bote.repository;

import Bote.model.CountEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by Paul Richter on Sat 24/07/2021
 */
@Repository
public interface CountEntryRepository extends CrudRepository<CountEntry, String> {

    Optional<CountEntry> findByToken(String messageCounter);

    @Transactional
    String deleteByToken(String token);

}
