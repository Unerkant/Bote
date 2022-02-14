package Bote.repository;

import Bote.model.CountEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Paul Richter on Sat 24/07/2021
 */
@Repository
public interface CountEntryRepository extends CrudRepository<CountEntry, String> {

    //Optional<SettingEntry> findByToken(String messageCounter);
}
