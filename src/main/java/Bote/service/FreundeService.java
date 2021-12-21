package Bote.service;

import Bote.model.Freunde;
import Bote.model.User;
import Bote.repository.FreundeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

   /* Neuer mein Chat Registrieren */
   public void saveMeinChat(Freunde freunde) {

      freundeRepository.save(freunde);
   }

   /* Neuer Freund Chat Registrieren */
   public void saveFreundChat(Freunde freunde) {

      freundeRepository.save(freunde);
   }

}
