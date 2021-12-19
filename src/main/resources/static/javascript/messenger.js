/*
*   Den 15.11.2021
*/


   /**
    *   Textarea Auto Rows
    */
  function autoRow(element){
        //alert(element);
        element.style.height = '5px';
        element.style.height = (element.scrollHeight)+'px';
  }

   /**
    *   Clikc aus Freunden oberfläche(Chart Starten)
    *   Functioniert nur bei Handy keine Destop Version
    *
    *   only-screen unter 650px: schaltet den Freunde div aus
    *   und blendet den Message div ein,
    *   unter function(zuruck) macht das umgekehrt
    *
    *   Daten werden von messenger.html gelesen
    */
  $('.freund').click(function(){
        //alert('Test');
        //var freundPseu  = $(this).attr('name');
        var freundId    = $(this).attr('id');
        var messageId   = $(this).attr('token');

       // alert(freundId+'/'+messageId);
        // Background nicht Active Freunde-Fenster zurück setzen
        var activ =  document.querySelectorAll('.freund');
            for( var i = 0; i < activ.length; i++){
                activ[i].style.background = 'transparent';
                activ[i].style.color = '#000';
            }
        $(this).css({"background-color":"#007ECC", "background":"linear-gradient(to right, #363795, #007ECC)", "color":"#FFF"});


        //alert('TEST' + id +' / '+ window.innerWidth);
        if(window.innerWidth < 651){
                document.getElementById("MESSAGEBOX").style.width = "100%";
                //$('#FREUNDEBOX').hide("slow");
                //$('#MESSAGEBOX').animate({ width: "toggle" });
        }else{
                // Ausgabe ID war im messenger.html/fragment:generalmessage.html(messagefragment)
                //$('#ID').text(freundId + '/' + messageId + '/' + name);
        }

        $.post('/fragmentmessages', {'freundeId': freundId, 'freundMessageId': messageId})
            .done(function(data){
                $('#MESSAGEFRAGMENT').replaceWith(data);
                $('.messageBody').scrollTop($('.messageBody')[0].scrollHeight);
                //alert(data);
            /**
             *  data: ist einen SeitenQuellText von fragments/generalmessage/messagefragment
             *  zugesendet von MessengerController.java @PostMapping mit return "messenger :: #MESSAGEFRAGMENT";
             *  den SeitenQuellText wird in messenger.html #MESSAGEFRAGMENT eingeblendet
             */
        });
  });

       /**
        *   Die click function hat nur eine aufgabe:
        *   den Message div ausblenden und Freunde div einblenden
        */
      function messageZuruck(){
            event.preventDefault();
            document.getElementById("MESSAGEBOX").style.width = "0%";
            //$("#MESSAGEBOX").hide("slow");
            //$('#FREUNDEBOX').animate({ width: 'toggle' });
      }


    /* ************************************************************ */
    /*  Web Socket + Neue Nachriichten Senden + aktualisieren       */
    /* ************************************************************ */

var stompClient = null;

function connect() {
    var socket = new SockJS('/register');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        //setConnected(true);
        //console.log('Connected: ' + frame);
        // Socket: output von MessageController
        stompClient.subscribe('/messages/receive', function (message) {
            //var message = JSON.parse(message.body);
            // output ausgabe( unten in function )
            //alert(message.datum +'/'+ message.token +'/'+ message.role +'/'+ message.messagetoken);
            //alert(message.text);
                fragmentAktualisieren();
                $('.messageBody').scrollTop($('.messageBody')[0].scrollHeight);
        });
    });
}
connect();

    /* **************************************************************** */
    /*  function sendeMessage() wird gestartet in                       */
    /*  generalmessage.html / fragment: messagefragment                 */
    /* **************************************************************** */

function sendeMessage(meinePseudonym, meineToken, messageToken) {

    // Datum anlegen für die Message Jahr zwei stellig und ohne Sekunden
    var d = new Date();
    var datum = ("0" + d.getDate()).slice(-2) + "." + ("0"+(d.getMonth()+1)).slice(-2) + "." +
    d.getFullYear().toString().substr(2,2) + " " + ("0" + d.getHours()).slice(-2) + ":" + ("0" + d.getMinutes()).slice(-2);

    var text = $("#MESSAGETEXT").val();

    // function stompFehler(): Fehler ausgabe in Fragment: messages.html Zeile:16
    if (stompClient == null) {
        stompFehler();
        return;
    }

    // Textarea auf Leer prüfen
    if(text.length === 0){
        return;
    }

    //<!-- Chat-Text sendem an MessageController.java  @MessageMapping/@SendTo -->
    //<!-- H2 Datenbank reihefolge: datum, messagetoken, name, pseudonym, role,text, vorname-->
   stompClient.send("/app/messages", {}, JSON.stringify({ 'datum': datum, 'messagetoken': messageToken, 'vorname':'', 'name': '',
                                       'pseudonym': meinePseudonym, 'meintoken': meineToken, 'text': text, 'role': 'default' }));

    $("#MESSAGETEXT").val('');
    $("#MESSAGETEXT").rows = 1;
   // $("#conversation").scrollTop = $("#conversation").scrollHeight;
}

    /* **************************************************************** */
    /* Nach dem Erfolgreichen versenden wirden die ganze Daten neu      */
    /* geladen von den H2:Datenbank von die Table MESSAGES und          */
    /* schliesslich in den Fragment: messagefragment angezeigt          */
    /*                                                                  */
    /* diese function wird in function:connect gestartet                */
    /* **************************************************************** */
function fragmentAktualisieren(){

        // Zwieschen gespeicherte Daten von fragments:messages.html Zeile: 58 holen
        // werden benötigt um neue chat-nachricht zu Aktualisieren
        // gesendet an MessageCotroller.java an @PostMapping

         var freundId    = $('#frTok').attr('name');
         var messageId   = $('#msTok').attr('name');
        //alert(freundId+'/'+messageId);
 $.post('/fragmentmessages', {'freundeId': freundId, 'freundMessageId': messageId})
            .done(function(data){
                $('#MESSAGEFRAGMENT').replaceWith(data);
                $('.messageBody').scrollTop($('.messageBody')[0].scrollHeight);
                 /**
                  *  data: ist einen SeitenQuellText von fragments/generalmessage/messagefragment
                  *  zugesendet von MessengerController.java @PostMapping mit return "messenger :: #MESSAGEFRAGMENT";
                  *  den SeitenQuellText wird in messenger.html #MESSAGEFRAGMENT eingeblendet
                  */
        });
}