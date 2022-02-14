package Bote.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Paul Richter on Sat 24/07/2021
 */
@Entity(name = "countentry")
public class CountEntry {

    @Id
    @Column(nullable = false)
    private String key;

    private String token;

    @Column(nullable = false)
    private String value;



    public CountEntry() {
    }

    public CountEntry(String key, String token, String value) {
        this.key = key;
        this.token = token;
        this.value = value;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
