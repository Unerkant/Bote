   /*
    *   Den 15.11.2021
    */


   /**
    *   Senden mit Enter...
    *   wenn textarea leer ist, bei Enter-Press nichts tun... ansonsten Senden
    *   bei normalen message verfassung in 'else' den function autoRow() frei schalten
    */
    var autorow = false;
    window.addEventListener( "keydown", (event) => {

        if (event.defaultPrevented) {
                return;
                // Sollte nichts tun, wenn die Standardaktion abgebrochen wurde
        }

        var handled = false;
        var valmsg = $("#MESSAGETEXT").val();
        if (event.key === 'Enter' && valmsg.trim() == "") {

            // Leeres Enter press, nichts tun
            autorow = false;
            handled = true;

        } else if (event.key === 'Enter') {

            // Enter press, message senden
           /*
            *   der send-click startet eine function in messagecomponents.html Zeile: 125
            *   th:onClick="sendeMessage(.....
            */
            $("#send").click();
            textareaLeer();
            handled = true;

        } else {

            // autoRow frei schalten, schutz von Leeren Enter press
            autorow = true;

        }

        if(handled){
            // „Doppelte Aktion“ unterdrücken, wenn das Ereignis behandelt wird
            event.preventDefault();
        }

    },true );

   /**
    *   Textarea Auto Rows
    *   messagecomponents.html Zeile: 120 textarea
    */
    function autoRow(element){

         //variable autorow: schutz von Leeren Enter-press
        if(autorow){
            element.style.height = '5px';
            element.style.height = (element.scrollHeight)+'px';
        }

    }



   /**
    *   messenger.html, bei verkleinert den Browser die Rechte teil die Seite
    *   auf 100% zihen
    *
    *   bei ELSE wird zuerst den div mit dem class 'messageBody' auf leer geprüft
    *   wenn Leer ist: Freunde(linke Teil) anzeigen
    *   wenn mit message-Texten gefühlt(einen Chat verbindung aktiv ist) ist
    *   dann Rechte Teil(message ausgabe) anzeigen
    */
    window.addEventListener("resize", function () {
        if(window.innerWidth > 650){
            $('.rechtsBox').css('width','100%');
        }else{
            if($.trim($('.messageBody').html()) || $.trim($('.neuerChatBody').html()) ){
                $('.rechtsBox').css('width','100%');
            }else{
                $('.rechtsBox').css('width','0%');
            }
        }
    });



   /**
    *   Click aus Freunden oberfläche(Chart Starten)
    *   Funktioniert nur bei Handy keine Destop Version
    *
    *   style,css Zeile: 110
    *   only-screen unter 650px: schaltet den Freunde div aus
    *   und blendet den Message div ein,
    *   unter function(zurück) macht das umgekehrt
    *
    *   Daten werden von messenger.html gelesen
    */
    $('.freund').click(function(){
        //alert('Test');
        var meineId     = $(this).attr('name');
        var freundId    = $(this).attr('id');
        var messageId   = $(this).attr('token');
        var nameFragment= 'messagefragment';

        // zu connect function meine ID senden hier unten Zeile: 91
        connect(meineId, messageId);

        // Aktive Freunde-Box, hover effect zurück setzen
        hoverRemove();
        // angeklickte Freund hover effect setzen
        $(this).css({"background-color":"#CCEEFF"});
        // bei vekleiner den Browser die Rechte Teil den messenger.html zuerst anzeigen
        $('.rechtsBox').css('width','100%');

        // Daten an den MessageController senden
        $.post('/fragmentmessages', {'freundeId': freundId, 'freundMessageId': messageId, 'nameFragment': nameFragment})
            .done(function(data){
                      $('#MESSAGEFRAGMENT').replaceWith(data);
                      $('.messageBody').scrollTop($('.messageBody')[0].scrollHeight);
            /**
             *  data: ist einen SeitenQuellText von fragments/generalmessage/messagefragment
             *  zugesendet von MessengerController.java @PostMapping mit return "messenger :: #MESSAGEFRAGMENT";
             *  den SeitenQuellText wird in messenger.html #MESSAGEFRAGMENT eingeblendet
             */

              // Firefox erkennt keine html autofocus...
              $('#MESSAGETEXT').focus();
        });

    });


   /**
    *   onClick in messagecomponents.html Zeile: 39
    *   ..a href="#" th:onClick="messageSchliessen()">Zurück</a..
    *   ACHTUNG: funktioniere nur über > 650px
    */
    function messageSchliessen(){
        event.preventDefault();

        hoverRemove();
        messageLeeren();
    }


   /**
    *   onClick in messagecomponents.html Zeile: 36
    *   ..a href="#" th:onClick="messageZuruck()">Zurück</a..
    *   ACHTUNG: funktioniere nur unter < 650px
    */
    function messageZuruck(){

        event.preventDefault();

        hoverRemove();
        messageLeeren();
        $('.rechtsBox').css('width','0%');
    }


   /**
    *   Message Leeren...Rechte Seite
    *   messanger.html Zeile: 170, switch anzeige: <div data-th-case="*">
    */
    function messageLeeren(){

        $.post('/Leer', {})
        .done(function(data){

            $('#MESSAGEFRAGMENT').replaceWith(data);
        });

    }


   /**
    * Freunde hover effect aussetzen
    */
    function hoverRemove(){
        var aktiv =  document.querySelectorAll('.freund');
            for( var i = 0; i < aktiv.length; i++){
                aktiv[i].style.background = 'transparent';
                aktiv[i].style.color = '#000';
            }
    }



    /* ************************************************************ */
    /*  Web Socket + Neue Nachrichten Senden & Life Anzeigen       */
    /* ************************************************************ */

    var stompClient = null;

    function connect(meineId, messageId) {

        if (stompClient !== null) {
            stompClient.disconnect();
        }

    var socket = new SockJS('/register');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        // Socket: output von MessageController
        stompClient.subscribe('/messages/receive/' + meineId, function (message) {
            //var message = JSON.parse(message.body);
            //alert(message.datum +'/'+ message.meintoken +'/'+ message.role +'/'+ message.messagetoken);
            //alert(message.text);

            // LIVE AUSGABE in messagecomponents.html Zeile: 94
            var message = JSON.parse(message.body);
            var messageHTML = "<div class="+(message.meintoken == meineId ? 'messageBodyMy' : 'messageBodyFreund') +">"+
            "<section>&#160;</section>"+
            "<nav>"+
                "<table border='0' cellpadding='0' cellspacing='0'>"+
                    "<tr>"+
                        "<td colspan='3'><p>"+ message.text +"</p></td>"+
                    "</tr>"+
                    "<tr>"+
                        "<td>"+
                            "<small>"+ (message.name.length == 0 ? message.pseudonym.toLowerCase()  : message.vorname.toLowerCase()+' '+message.name.toLowerCase()) +"...</small>"+
                        "</dt>"+
                        "<td width='40'>"+
                            "<small>"+ message.datum.substring(8,14) +"</small>"+
                        "</td>"+
                        "<td width='20'>"+
                            "<img width='15' src='/img/done.png'>"+
                        "</td>"+
                    "</tr>"+
                "</table>"+
            "</nav>"+
            "<aside>&#160;</aside>"+
            "</div>";

                //$('#CHATBOX').html($('#CHATBOX').html()+ messageId +'/' + message.text +  "<br />");
                $('#CHATBOX').html($('#CHATBOX').html()+messageHTML);
                $('.messageBody').scrollTop($('.messageBody')[0].scrollHeight);
            });
        });
    }



    /* **************************************************************** */
    /*  function sendeMessage() wird gestartet in                       */
    /*  fragment: messagecomponents.html  Zeile 126                     */
    /* **************************************************************** */

    function sendeMessage(meinePseudonym, freundeToken, meineToken, messageToken) {

        var text = $("#MESSAGETEXT").val();
        if(text.length === 0){
            $("#MESSAGETEXT").focus();
            return;
        }

        // function stompFehler(): deffiniert in messagecomponents.html: messagesfragment.html Zeile:47
        if (stompClient == null) {
            stompFehler();
            return;
        }

        // Datum anlegen für die Message Jahr zwei stellig und ohne Sekunden
        var d = new Date();
        var datum = ("0" + d.getDate()).slice(-2) + "." + ("0"+(d.getMonth()+1)).slice(-2) + "." +
        d.getFullYear().toString().substr(2,2) + " " + ("0" + d.getHours()).slice(-2) + ":" +
        ("0" + d.getMinutes()).slice(-2);

        //<!-- Chat-Text sendem an MessageController.java  @MessageMapping/@SendTo -->
        //<!-- H2 Datenbank reihefolge: datum, messagetoken, name, pseudonym, role,text, vorname-->
        stompClient.send("/app/messages", {}, JSON.stringify({ 'datum': datum, 'freundetoken': freundeToken, 'meintoken': meineToken, 'messagetoken': messageToken,
                            'pseudonym': meinePseudonym, 'name': '', 'vorname':'', 'text': text, 'role': 'default' }));

        // textarea leeren
        textareaLeer();

    }


   /**
    *   setzt textarea in Start zustand
    */
    function textareaLeer(){
        $("#MESSAGETEXT").val("");
        $("#MESSAGETEXT").height(20);
        //$("#MESSAGETEXT").rows = 1;
        $("#MESSAGETEXT").focus();
    }