   /**
    *   Den 28.11.2021
    */


   /**
    *   ALLE REQUEST ANFRAGE an FreundeController.java GESENDET & Bearbeitet
    *
    *   Freunde Einladen( neuer Chat anlegen)
    *   nur für Fragment anzeigen messenger.html(Rechte Seite)
    */
    function neuerChat(){
        event.preventDefault();
        var namefragment = 'neuerchatfragment';
        $('.rechtsBox').css('width','100%');

       // Background Active Freunde-Fenster zurück setzen
        var activ =  document.querySelectorAll('.freund');
            for( var i = 0; i < activ.length; i++){
                activ[i].style.background = 'transparent';
                activ[i].style.color = '#000';
            }

    // Fragment Name: 'neuerchatfragment' in (fragments/messagecomponents/neuerchatfragment)
    $.post('/neuerchat', {'namefragment': namefragment})
        .done(function(data){
            $('#MESSAGEFRAGMENT').replaceWith(data);
            //$('.messageBody').scrollTop($('.messageBody')[0].scrollHeight);

            /**
             *  data: ist einen SeitenQuellText von Fragment (fragments/messagecomponents/neuerchatfragment)
             *  zugesendet von NeuerChatController.java @PostMapping("/neuerchat")
             *  mit return "messenger :: #MESSAGEFRAGMENT";
             *  den SeitenQuellText wird in messenger.html #MESSAGEFRAGMENT eingeblendet
             */
        });
    }

   /**
    *   Die click function hat nur eine aufgabe:
    *   den Message div ausblenden und Freunde div einblenden
    */
    function neuerChatZuruck(){
        event.preventDefault();
        $('.rechtsBox').css('width','0%');
    }

   /**
    *   neuer Freund Suchen(neuer Chat anlegen) und einladen
    */
    function neuerChatSuchen(form){
        event.preventDefault();
        var chatfragment = 'neuerchatfragment';
        var mailOderTel = form[1].value;
        var istZahl = null;

        /* Input auf Leer prüfen */
        if(mailOderTel.length === 0){
            $('#NEUERCHATFELDLEER').slideDown(600).delay(5000).slideUp(600);
            return false;
         }
        /* E-Mail Validieren */
        if(isNaN(mailOderTel) && mailOderTel.search(/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/) == -1){
           $('#KEINEGULTIGEMAIL').slideDown(600).delay(5000).slideUp(600);
           return false;
        }
        /* ermiteln ob eine e-Mail oder Telefon ist */
        if(!isNaN(mailOderTel)){ istZahl = 'ZAHL'; }

        //alert(form[0]+'/'+form[1]+'/'+form[2]);
        $.post('/bekantensuchen', {'mailodertel': mailOderTel, 'chatfragment': chatfragment, 'zahl': istZahl})
        .done(function(output){
             //alert(output);
            // #MESSAGEFRAGMENT befindet sich in messenger.html Zeile: 184
            $('#MESSAGEFRAGMENT').replaceWith(output);
        });
    }


   /**
    *   Neuer Chat Einladen Speichenr in die Tabelle Freunde
    *   Daten zugesendet von messagecomponents.html Zeile: 232
    *   data-th-onClick="bekantenSave([[${gefunden.token}]])"
    *   FreundeController.html Zeile: 202
    */
    function bekantenSave(bekantenToken){
        event.preventDefault();
        //alert(freundToken);
        $.post('/bekantensave', {'bekantentoken': bekantenToken})
            .done(function(out){
                //alert(out);
                // Ausgabe in messenger.html Zeile:64
                $('#NEUERCHAT').replaceWith(out);

                // bei Große den Fenster unter 650px wird die Rechte Seite
                // geschlossen und ausgeblendet
                if(window.innerWidth < 650){
                    neuerChatZuruck();
                }
                $('.neuerChat').delay(100).fadeOut();
            });
    }

   /**
    *   Update: Eiladung zum Neuer Chat annehmen
    *   Löschen in den Datenbank Tabelle Freunde: role inhalt(Löschen: werdeeingeladen/warteaufok)
    */
    function einladungAnnehmen(mesToken){
        event.preventDefault();
        $.post('/einladungUpdate', {'messageToken': mesToken})
        .done(function(eingeladen){
            //alert(eingeladen);
            $('#BE').slideUp(600);
            setTimeout(function(){ window.location = "/messenger"; }, 2000);
        });
    }

   /**
    *   Freunde Löschen
    *   Anzeige nur für den Roten knopf
    */
    function freundeBearbeiten(){
        event.preventDefault();
         var item =  document.querySelectorAll('.freundeRemove');
            for( var i = 0; i < item.length; i++){
               $(item[i]).toggle();
            }
        //$('.freund').click(function(){ return false; }
    }

   /**
    *   Freunde Löschen ( Chat Löschen)
    *   Löschen in den Datenbank: FREUNDE,  2 eiträgen(meinen & freund)
    *   mit den WHERE: messagetoken
    *
    *   Benutzt von messenger.html Zeile: 77 & bekanteneinladung.html(fragment) Zeile: 54
    */
    function freundeRemove(messToken){
        event.preventDefault();
        $.post('/freundedelete', {'messToken': messToken})
            .done(function(data){

               //$('#OK'+id).replaceWith(data);
               // Ausgabe in messenger.html Zeile: 107
               $('#OK'+messToken).slideDown(600).delay(5000).slideUp(600);
               $('#'+messToken).delay(5000).slideUp(600);
               setTimeout(function(){ window.location = "/messenger"; }, 2000);
        });
    }


