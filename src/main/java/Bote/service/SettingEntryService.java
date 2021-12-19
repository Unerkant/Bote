package Bote.service;

import Bote.model.SettingEntry;
import Bote.model.User;
import Bote.repository.SettingEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Paul Richter on Sat 24/07/2021
 */
@Service
public class SettingEntryService {

    public static String MESSAGE_COUNTER = "MESSAGE_COUNTER";

    @Autowired
    private SettingEntryRepository settingEntryRepository;

    User token = new User();

    public int incrementMessageCounter() {
        SettingEntry settingEntry = settingEntryRepository.findById(MESSAGE_COUNTER).orElse(new SettingEntry(MESSAGE_COUNTER, "0", token.getToken()));

        int counterValue = Integer.parseInt(settingEntry.getValue());
        counterValue++;

        settingEntry.setValue("" + counterValue);
        settingEntryRepository.save(settingEntry);

        return counterValue;
    }
}
