package Bote.repository;

import Bote.model.SettingEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by Paul Richter on Sat 24/07/2021
 */
@Repository
public interface SettingEntryRepository extends CrudRepository<SettingEntry, String> {

    //Optional<SettingEntry> findByToken(String messageCounter);
}
