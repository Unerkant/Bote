package Bote.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Paul Richter on Sat 24/07/2021
 */
@Entity(name = "countentry")
public class CountEntry {

    @Id
    @GeneratedValue
    private long id;

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private String datum;

    private String token;

    private String total;


    public CountEntry() {
    }

    public CountEntry(long id, String datum, String token, String total) {
        this.id = id;
        this.datum = datum;
        this.token = token;
        this.total = total;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getDatum() { return datum; }
    public void setDatum(String datum) { this.datum = datum; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTotal() {
        return total;
    }
    public void setTotal(String total) {
        this.total = total;
    }
}
