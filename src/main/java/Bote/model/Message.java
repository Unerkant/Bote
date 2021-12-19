package Bote.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by Paul Richter on Mon 19/07/2021
 */

@Entity(name = "MESSAGES")
public class Message {

    @Id
    @GeneratedValue
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String datum;
    private String messagetoken;
    private String vorname;
    private String name;
    private String pseudonym;
    private String meintoken;
    private String text;
    private String role;



    public Message() {
        // empty
    }

    public Message(Long id, String datum, String messagetoken, String vorname, String name, String pseudonym, String meintoken , String text, String role ) {
        this.id = id;
        this.datum = datum;
        this.messagetoken = messagetoken;
        this.vorname = vorname;
        this.name = name;
        this.pseudonym = pseudonym;
        this.meintoken = meintoken;
        this.text = text;
        this.role = role;
    }


    public Long getId() {
        return id;
    }
    public void setId(Long id) { this.id = id; }

    public String getDatum() { return datum; }
    public void setDatum(String datum) { this.datum = datum; }

    public String getMessagetoken() { return messagetoken; }
    public void setMessagetoken(String messagetoken) { this.messagetoken = messagetoken; }

    public String getVorname() { return vorname; }
    public void setVorname(String vorname) { this.vorname = vorname; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPseudonym() {
        return pseudonym;
    }
    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }

    public String getMeintoken() { return meintoken; }
    public void setMeintoken(String meintoken) { this.meintoken = meintoken; }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", datum='" + datum + '\'' +
                ", messagetoken='" + messagetoken + '\'' +
                ", vorname='" + vorname + '\'' +
                ", name='" + name + '\'' +
                ", pseudonym='" + pseudonym + '\'' +
                ", meintoken='" + meintoken + '\'' +
                ", text='" + text + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
