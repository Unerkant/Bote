package Bote.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;


@Entity
public class Phone {

    @Id
    @GeneratedValue
    private long id;

    private String bild;

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private String datum;

    private String token;

    private String pseudonym;

    private String name;

    private String vorname;

    private String phoneart;

    private String dauert;

    private String role;


    public Phone(){
        //Leer
    }

    public Phone(long id, String bild, String datum, String token, String pseudonym, String name,
                 String vorname, String phoneart, String dauert, String role)
    {
       this.id          = id;
       this.bild        = bild;
       this.datum       = datum;
       this.token       = token;
       this.pseudonym   = pseudonym;
       this.name        = name;
       this.vorname     = vorname;
       this.phoneart    = phoneart;
       this.dauert      = dauert;
       this.role        = role;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getBild() { return bild; }
    public void setBild(String bild) { this.bild = bild; }

    public String getDatum() { return datum; }
    public void setDatum(String datum) { this.datum = datum; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getPseudonym() { return pseudonym; }
    public void setPseudonym(String pseudonym) { this.pseudonym = pseudonym; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getVorname() { return vorname; }
    public void setVorname(String vorname) { this.vorname = vorname; }

    public String getPhoneart() {  return phoneart; }
    public void setPhoneart(String phoneart) { this.phoneart = phoneart; }

    public String getDauert() { return dauert; }
    public void setDauert(String dauert) { this.dauert = dauert; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }


    @Override
    public String toString() {
        return "Phone{" +
                "id='" + id + '\'' +
                ", bild='" + bild + '\'' +
                ", datum='" + datum + '\'' +
                ", token='" + token + '\'' +
                ", pseudonym='" + pseudonym + '\'' +
                ", name='" + name + '\'' +
                ", vorname='" + vorname + '\'' +
                ", phoneart='" + phoneart + '\'' +
                ", dauert='" + dauert + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
