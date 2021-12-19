/**
 *   Den 28.11.2021
 */

    /* **************************************************************** */
    /*                Neuen Chat(Kontakt) hinzufügen                    */
    /*  fragment ausgabe in messenger.html/#NEUERCHAT(rechte teil)      */
    /*                                                                  */
    /*  einen onClick im den Chat auf den Bild User-Plus:               */
    /*  wird mit dem Post meinen Token an                               */
    /*  FreundeController.java/@PostMapping(neuerchat) gesendet,        */
    /*  und aus dem H2 Datenbank alle Registrierte User geladen und mit */
    /*  den return an fragment:#NEUERCHAT ubergeben und schliesslich    */
    /*  mit done:data (Zeile 28) ausgegeben:                            */
    /*  $('#NEUERCHAT').replaceWith(data);)                             */
    /* **************************************************************** */
function neuerChat(meinId){
   // alert('Test');
    event.preventDefault();
    $('#MESSAGEFRAGMENT').hide();
    $('#NEUERCHAT').animate({ width: 'toggle' });

            // Background nicht Active Fenster zurück setzen
            var activ =  document.querySelectorAll('.freund');
                for( var i = 0; i < activ.length; i++){
                    activ[i].style.background = 'transparent';
                    activ[i].style.color = '#000';
                }

     if(window.innerWidth < 651){
            document.getElementById("MESSAGEBOX").style.width = "100%";
         //$('#FREUNDEBOX').hide("slow");
         // $('#MESSAGEBOX').animate({ width: "toggle" });
     }

     $.post('/neuerchat', {'meinId': meinId})
           .done(function(data){
           $('#NEUERCHAT').replaceWith(data);
           //alert(data);
                /**
                 *  data: ist einen 'SeitenQuellText' von fragments/generalmessage/neuerchat,
                 *  zugesendet von MessengerController.java @PostMapping mit return "messenger :: #NEUERCHAT";
                 *  den SeitenQuellText wird in messenger.html #NEUERCHAT eingefügt
                 */
     });
}

  /**
    *  Radio Button checked setzen und Sende Buttons frei schalten
    *
    *   HINWEIS: wenn lassen sie die 'Form' tag aus wird automatisch
    *   nur einen Radio button selected(zugelassen)
    *
    *  bei mehreren anClicken von Radio buttons...wird
    *  mit querySelectorAll alle button mit dem namen: neuerchatbutton
    *  durchlaufen und alle enabled buttons(if abfrage) wieder disabled gesetzt
    *
    *  schliesslich wird angeclickte button wieder frei geschaltet
    */
function radioButton(buttonId){

    var buttons =  document.querySelectorAll('[name="neuerchatbutton"]');
    for( var i = 0; i < buttons.length; i++){
       if(buttons[i].disabled == false){
            buttons[i].setAttribute('disabled','disabled');
       }
    }

    $('#'+buttonId).removeAttr('disabled');
}


  /**
    *   Kontakt(Chat) hinzufügen
    *   in H2 Datenbank
    *   Neuen Freund
    */
function neuerFreundSave(freundToken, freundPseu, meinToken, meinPseu){
    event.preventDefault();
    //alert('Neuer Freund' + freundId +'/'+freundPseu +'/'+myId +'/'+ myPseu);
    $.post('/neuerchatsave', {'freundToken': freundToken, 'freundPseu': freundPseu, 'meinToken': meinToken, 'meinPseu': meinPseu})
         .done(function(data){
             //$('#NEUERCHAT').replaceWith(data);
             window.location.href = "/messenger";
             //alert(data);
                    /**
                     *  data: ist einen 'SeitenQuellText' von messenger.html/ Linke teil nur #FREUNDEBOX,
                     *  zugesendet von FreundeController.java @PostMapping mit return "redirect:/messenger";;
                     *
                     *  die return wird einfach ignoriert und Seite neu Geladen mit dem Neuen Freund(Chat)
                     */
    });
}