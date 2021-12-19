package Bote.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Paul Richter on Sat 24/07/2021
 */
@Entity(name = "SETTING_ENTRIES")
public class SettingEntry {

    @Id
    @Column(nullable = false)
    private String key;

    @Column(nullable = false)
    private String value;

    private String token;

    public SettingEntry() {

    }

    public SettingEntry(String key, String value, String token) {
        this.key = key;
        this.value = value;
        this.token = token;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }
}
