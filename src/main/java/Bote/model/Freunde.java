package Bote.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;

/**
   *    Den 1.11.2021
   */

@Entity
public class Freunde {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String datum;

    private String freundetoken;

    @Column(nullable = false)
    private String freundpseudonym;

    private String meinentoken;

    @Column(nullable = false)
    private String meinpseudonym;

    private String messagetoken;

    @Column(nullable = false)
    private String role;

    public Freunde(){
        //Leer
    }

    public Freunde(long id, String datum, String freundetoken, String freundpseudonym, String meinentoken, String meinpseudonym, String messagetoken, String role){
        this.id = id;
        this.datum = datum;
        this.freundetoken = freundetoken;
        this.freundpseudonym = freundpseudonym;
        this.meinentoken = meinentoken;
        this.meinpseudonym = meinpseudonym;
        this.messagetoken = messagetoken;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getFreundetoken() {
        return freundetoken;
    }

    public void setFreundetoken(String freundetoken) {
        this.freundetoken = freundetoken;
    }

    public String getFreundpseudonym() {
        return freundpseudonym;
    }

    public void setFreundpseudonym(String freundpseudonym) {
        this.freundpseudonym = freundpseudonym;
    }

    public String getMeinentoken() {
        return meinentoken;
    }

    public void setMeinentoken(String meinentoken) {
        this.meinentoken = meinentoken;
    }

    public String getMeinpseudonym() {
        return meinpseudonym;
    }

    public void setMeinpseudonym(String meinpseudonym) {
        this.meinpseudonym = meinpseudonym;
    }

    public String getMessagetoken() {
        return messagetoken;
    }

    public void setMessagetoken(String messagetoken) {
        this.messagetoken = messagetoken;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    @Override
    public String toString() {
        return "Freunde{" +
                "id='" + id + '\'' +
                ", datum='" + datum + '\'' +
                ", fruendetoken='" + freundetoken + '\'' +
                ", freundpseudonym='" + freundpseudonym + '\'' +
                ", meinentoken='" + meinentoken + '\'' +
                ", meinpseudonym='" + meinpseudonym + '\'' +
                ", messagetoken='" + messagetoken + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
