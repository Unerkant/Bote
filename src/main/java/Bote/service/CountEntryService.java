package Bote.service;

import Bote.configuration.GlobalConfig;
import Bote.model.CountEntry;
import Bote.repository.CountEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Paul Richter on Sat 24/07/2021
 */
@Service
public class CountEntryService {

    @Autowired
    private MethodenService methodenService;
    @Autowired
    private CountEntryRepository countEntryRepository;

    private String datum;



    /**
     * Zählt alle message von einem User, sortiert nach dem token
     * benötigt in MessageController.java Zeile: 150
     *
     */
    public int incrementMessageCounter( String meintoken) {

        datum = methodenService.aktuellTag();

        // Wenn keine Daten vorhanden sind dann eintragen
        CountEntry settingEntry = countEntryRepository
                .findByToken(meintoken)
                .orElse(new CountEntry(1, datum,  meintoken, "0"));

        // Letzten count aus der Datenbank holen
        int counterValue = Integer.parseInt(settingEntry.getTotal());
        counterValue++;

        // Datum in Datenbank aktualisieren einmal pro Tag
        String letzteDatum = settingEntry.getDatum();
        if ( Integer.parseInt(datum) > Integer.parseInt(letzteDatum) ) {
            settingEntry.setDatum(datum);
        }

        // Daten Aktualisieren/Speichern
        settingEntry.setTotal("" + counterValue);
        countEntryRepository.save(settingEntry);

        return counterValue;
    }



    /**
     * benutzt von SettingController.java Zeile: 363
     * Löschen message count, nach token
     */
    public String countEntryLoschen(String token){

        return countEntryRepository.deleteByToken(token);
    }
}
