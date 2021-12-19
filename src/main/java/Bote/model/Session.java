package Bote.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;

@Entity
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String token;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String datum;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String letztenlogin;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String letztenoutlog;

    private String other;

    public Session(){}

    public Session(long id, String token, String datum, String letztenlogin, String letztenoutlog, String other){
        this.id =id;
        this.token = token;
        this.datum  = datum;
        this.letztenlogin = letztenlogin;
        this.letztenoutlog = letztenoutlog;
        this.other = other;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getLetztenlogin() {
        return letztenlogin;
    }

    public void setLetztenlogin(String letztenlogin) {
        this.letztenlogin = letztenlogin;
    }

    public String getLetztenoutlog() {
        return letztenoutlog;
    }

    public void setLetztenoutlog(String letztenoutlog) {
        this.letztenoutlog = letztenoutlog;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id='" + id + '\'' +
                ", token='" + token + '\'' +
                ", datum='" + datum + '\'' +
                ", letztenlogin='" + letztenlogin + '\'' +
                ", letztenoutlog='" + letztenoutlog + '\'' +
                ", other='" + other + '\'' +
                '}';
    }


}
