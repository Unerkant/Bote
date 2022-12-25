package Bote.service;

import Bote.model.Freunde;
import Bote.repository.FreundeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

   /**
    *    Den 1.10.2021
    */
@Service
public class FreundeService {

   @Autowired
   private FreundeRepository freundeRepository;
   @Autowired
   private MessageService messageService;


   /**
    *  Nach Meinen Freuden suchen
    */
   public List<Freunde> freundeSuchen(String meinentoken){
      List<Freunde> freund = new ArrayList<>();
      freundeRepository.findByMeinentoken(meinentoken).forEach(freund::add);
      return freund;
   }


   /**
    * diese Methode wird von ApiFreundeController benutzt,
    * an Freunde-array wird letzte message-nachricht angehängt und mit den
    * ApiFreundecontroller an den BoteFx als responseBody gesendet
    *
    * @param meinentoken
    * @return
    */
   public List<Freunde> freundeSuchenMitLetzterNachricht(String meinentoken) {
      List<Freunde> freunde = freundeSuchen(meinentoken);
      messageService.setzeLetzteNachrichten(freunde);
      return freunde;
   }


   /**
    *  functioniert 100% - zurzeit nicht benutzt
    */
   public List<Freunde> findeMessagetoken(String token){

      return freundeRepository.findByMessagetoken(token);
   }


   /**
    * functionier 100% - zurzeit nicht benutzt
    *
    * war gedacht für den ApiUserController/einladenApi,
    * nicht 2-mal einladen
    *
    * @param mail
    * @return
    */
   public List<Freunde> findeMail(String mail){
      return freundeRepository.findByFreundemail(mail);
   }


   /**
    *  Meinen NeuerChat Registrieren
    */
   public void saveMeinChat(Freunde freunde) {

      freundeRepository.save(freunde);
   }


   /**
    *  Freunde NeuerChat Registrieren
    */
   public void saveFreundChat(Freunde freunde) {

      freundeRepository.save(freunde);
   }


   /**
    *  Update role (Löschen von 2x eiträgen in die Spalte role, einladung freischalten)
    */
   public Integer roleUpdate(String role, String token){

      return freundeRepository.updateRole(role, token);
   }


   /**
    *    Freund Löschen
    *
    *    1. benutzt von FreundeController/ @PostMapping(value = "/freundedelete")
    *       Quelle: button/messenger.html Zeile: 79 (FreundRemove()) -> freunde.js Zeile: 138
    *    2. benutzt von ApiFreundeController Zeile: 80
    *
    */
   @Transactional
   public List<Freunde> freundLoschen(String token){

      return freundeRepository.deleteByMessagetoken(token);
   }


   /**
    *   benutzt von SettingController/@PostMapping(value = "/accountloschen")
    *   messagetoken ist einen array [26022022183542, 14032022172344]
    *   mit diesem array werden in Tabelle: Freunde gleich 4(in diesem fall) einträgen gelöscht,
    *   von mir und freund(wir besitzen die gleiche Message Token)
    */
   @Transactional
   public String allFreundeLoschen(List<String> messagetoken){

         return freundeRepository.deleteByMessagetokenIn(messagetoken);
   }

}
