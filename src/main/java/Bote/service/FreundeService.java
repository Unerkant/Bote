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

   /* Nach Meinen Freuden suchen */
   public List<Freunde> freundeSuchen(String meinentoken){
      List<Freunde> freund = new ArrayList<>();
      freundeRepository.findByMeinentoken(meinentoken).forEach(freund::add);
      return freund;
   }


   /* functioniert 100% - zurzeit nicht benutzt */
   public List<Freunde> findeMessagetoken(String token){

      return freundeRepository.findByMessagetoken(token);
   }

   /* Meinen NeuerChat Registrieren */
   public void saveMeinChat(Freunde freunde) {

      freundeRepository.save(freunde);
   }


   /* Freunde NeuerChat Registrieren */
   public void saveFreundChat(Freunde freunde) {

      freundeRepository.save(freunde);
   }


   /* Update role (Löschen von 2x eiträgen in die Spalte role, einladung freischalten) */
   public Integer roleUpdate(String role, String token){

      return freundeRepository.updateRole(role, token);
   }


   /**
    *    bentzt von FreundeController/ @PostMapping(value = "/freundedelete")
    *    Freund loschen
    */
   @Transactional
   public void freundLoschen(String token){

      freundeRepository.deleteByMessagetoken(token);
   }

   /**
    *   benutzt von SettingController/@PostMapping(value = "/accountloschen")
    *   messagetoken ist einen array [26022022183542, 14032022172344]
    *   mit diesem array werden in Tabelle: Freunde gleich 4 einträgen gelöscht,
    *   von mir und freund(wir besitzen die gleiche Message Token)
    */
   @Transactional
   public String allFreundeLoschen(List<String> messagetoken){

         return freundeRepository.deleteByMessagetokenIn(messagetoken);
   }

   }
