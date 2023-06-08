package Bote.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;


/**
    *    Den 1.11.2021
    */

@Entity
public class Freunde {

    @Id
    @GeneratedValue
    private long id;
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private String datum;
    private String meinentoken;
    private String messagetoken;
    private String freundetoken;
    private String freundebild;
    private String freundepseudonym;
    private String freundename;
    private String freundevorname;
    private String freundemail;
    private String freundetelefon;
    private String role;
    @Transient // Bedeutet wird nicht für die Datenbank verwendet
    private String letzteNachricht;
    @Transient // Bedeutet wird nicht für die Datenbank verwendet
    private String datumLetzteNachricht;


    public Freunde(){
        //Leer
    }


    public Freunde(long id, String datum, String meinentoken, String messagetoken, String freundetoken, String freundebild,
                      String freundepseudonym, String freundename, String freundevorname, String freundemail,
                      String freundetelefon, String role, String letzteNachricht, String datumLetzteNachricht )
    {
        this.id                     = id;
        this.datum                  = datum;
        this.meinentoken            = meinentoken;
        this.messagetoken           = messagetoken;
        this.freundetoken           = freundetoken;
        this.freundebild            = freundebild;
        this.freundepseudonym       = freundepseudonym;
        this.freundename            = freundename;
        this.freundevorname         = freundevorname;
        this.freundemail            = freundemail;
        this.freundetelefon         = freundetelefon;
        this.role                   = role;
        this.letzteNachricht        = letzteNachricht;
        this.datumLetzteNachricht   = datumLetzteNachricht;
    }


    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getDatum() { return datum; }
    public void setDatum(String datum) { this.datum = datum; }

    public String getMeinentoken() { return meinentoken; }
    public void setMeinentoken(String meinentoken) { this.meinentoken = meinentoken; }

    public String getMessagetoken() { return messagetoken; }
    public void setMessagetoken(String messagetoken) { this.messagetoken = messagetoken; }

    public String getFreundetoken() { return freundetoken; }
    public void setFreundetoken(String freundetoken) { this.freundetoken = freundetoken; }

    public String getFreundebild() { return freundebild; }
    public void setFreundebild(String freundebild) { this.freundebild = freundebild; }

    public String getFreundepseudonym() { return freundepseudonym; }
    public void setFreundepseudonym(String freundepseudonym) { this.freundepseudonym = freundepseudonym; }

    public String getFreundename() { return freundename; }
    public void setFreundename(String freundename) { this.freundename = freundename; }

    public String getFreundevorname() { return freundevorname; }
    public void setFreundevorname(String freundevorname) { this.freundevorname = freundevorname; }

    public String getFreundemail() { return freundemail; }
    public void setFreundemail(String freundemail) { this.freundemail = freundemail; }

    public String getFreundetelefon() { return freundetelefon; }
    public void setFreundetelefon(String freundetelefon) { this.freundetelefon = freundetelefon; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getLetzteNachricht() {
        return letzteNachricht;
    }
    public void setLetzteNachricht(String letzteNachricht) {
        this.letzteNachricht = letzteNachricht;
    }

    public String getDatumLetzteNachricht() {
        return datumLetzteNachricht;
    }
    public void setDatumLetzteNachricht(String datumLetzteNachricht) {
        this.datumLetzteNachricht = datumLetzteNachricht;
    }




    @Override
    public String toString() {
        return "Freunde{" +
                "id='" + id + '\'' +
                ", datum='" + datum + '\'' +
                ", meinentoken='" + meinentoken + '\'' +
                ", messagetoken='" + messagetoken + '\'' +
                ", fruendetoken='" + freundetoken + '\'' +
                ", fruendebild='" + freundebild + '\'' +
                ", freundepseudonym='" + freundepseudonym + '\'' +
                ", freundename='" + freundename + '\'' +
                ", freundevorname='" + freundevorname + '\'' +
                ", freundemail='" + freundemail + '\'' +
                ", freundetelefon='" + freundetelefon + '\'' +
                ", role='" + role + '\'' +
                ", letzteNachricht='" + letzteNachricht + '\'' +
                ", datumLetzteNachricht='" + datumLetzteNachricht + '\'' +
                '}';
    }

}
