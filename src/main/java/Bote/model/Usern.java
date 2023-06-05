package Bote.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.*;

/**
 * Created by Paul Richter on Fri 20/08/2021
 */

@Entity
public class Usern {

    @Id
    @GeneratedValue
    private long id;
    private String token;
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private String datum;
    private String bild;
    private  String pseudonym;
    private String name;
    private String vorname;
    private String email;
    private  String telefon;
    private String role;
    private  String other;



    public Usern(){
        //Leer
    }


    public Usern(long id, String token, String datum, String bild, String pseudonym, String name,
                 String vorname, String email, String telefon, String role, String other )
    {
        this.id = id;
        this.token = token;
        this.datum = datum;
        this.bild = bild;
        this.pseudonym = pseudonym;
        this.name = name;
        this.vorname = vorname;
        this.email = email;
        this.telefon = telefon;
        this.role = role;
        this.other = other;
    }


    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getDatum() { return datum; }
    public void setDatum(String datum) { this.datum = datum; }

    public String getBild() { return bild; }
    public void setBild(String bild) { this.bild = bild; }

    public String getPseudonym() { return pseudonym; }
    public void setPseudonym(String pseudonym) { this.pseudonym = pseudonym; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getVorname() { return vorname; }
    public void setVorname(String vorname) { this.vorname = vorname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getOther() { return other; }
    public void setOther(String other) { this.other = other; }


    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", token='" + token + '\'' +
                ", datum='" + datum + '\'' +
                ", bild='" + bild + '\'' +
                ", pseudonym='" + pseudonym + '\'' +
                ", name='" + name + '\'' +
                ", vorname='" + vorname + '\'' +
                ", email='" + email + '\'' +
                ", telefon='" + telefon + '\'' +
                ", role='" + role + '\'' +
                ", other='" + other + '\'' +
                '}';
    }

}
