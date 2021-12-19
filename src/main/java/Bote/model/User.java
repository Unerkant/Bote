package Bote.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;

/**
 * Created by Paul Richter on Fri 20/08/2021
 */

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String bild;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String datum;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private  String pseudonym;

    private String role;

    @Column(nullable = false)
    private  String telefon;

    private  String token;

    private String vorname;

    public User(){
        //Leer
    }

    public User(long id, String bild, String datum, String email, String name, String pseudonym, String role, String telefon, String token, String vorname){
        this.id = id;
        this.bild = bild;
        this.datum = datum;
        this.email = email;
        this.name = name;
        this.pseudonym = pseudonym;
        this.role = role;
        this.telefon = telefon;
        this.token = token;
        this.vorname = vorname;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getBild() { return bild; }
    public void setBild(String bild) { this.bild = bild; }

    public String getDatum() { return datum; }
    public void setDatum(String datum) { this.datum = datum; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPseudonym() { return pseudonym; }
    public void setPseudonym(String pseudonym) { this.pseudonym = pseudonym; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getVorname() { return vorname; }
    public void setVorname(String vorname) { this.vorname = vorname; }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", bild='" + bild + '\'' +
                ", datum='" + datum + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", pseudonym='" + pseudonym + '\'' +
                ", role='" + role + '\'' +
                ", telefon='" + telefon + '\'' +
                ", token='" + token + '\'' +
                ", vorname='" + vorname + '\'' +
                '}';
    }

}
