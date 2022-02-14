package Bote.service;

import Bote.model.CountEntry;
import Bote.model.User;
import Bote.repository.CountEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Paul Richter on Sat 24/07/2021
 */
@Service
public class CountEntryService {

    public static String MESSAGE_COUNTER = "MESSAGE_COUNTER";

    @Autowired
    private CountEntryRepository countEntryRepository;

    User token = new User();

    public int incrementMessageCounter() {
        CountEntry settingEntry = countEntryRepository.findById(MESSAGE_COUNTER).orElse(new CountEntry(MESSAGE_COUNTER, "0", String.valueOf(token.getToken())));

        int counterValue = Integer.parseInt(settingEntry.getValue());
        counterValue++;

        settingEntry.setValue("" + counterValue);
        countEntryRepository.save(settingEntry);

        return counterValue;
    }
}
