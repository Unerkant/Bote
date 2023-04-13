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
        var meineId     = $(this).attr('name');
        var freundId    = $(this).attr('id');
        var messageId   = $(this).attr('token');
        var nameFragment= 'messagefragment';

        // zu connect function meine ID senden hier unten Zeile: 91
        connect(meineId, messageId);

        // Background nicht Active Freunde-Fenster zurück setzen
        var activ =  document.querySelectorAll('.freund');
            for( var i = 0; i < activ.length; i++){
                activ[i].style.background = 'transparent';
                activ[i].style.color = '#000';
            }
        $(this).css({"background-color":"#2980B9", "background":"linear-gradient(to left, #FFFFFF, #6DD5FA, #2980B9)", "color":"#000"});
         // bei vekleiner den Browser die Rechte Teil den messenger.html zuerst anzeigen
        $('.rechtsBox').css('width','100%');

        $.post('/fragmentmessages', {'freundeId': freundId, 'freundMessageId': messageId, 'nameFragment': nameFragment})
            .done(function(data){
                      $('#MESSAGEFRAGMENT').replaceWith(data);
                      $('.messageBody').scrollTop($('.messageBody')[0].scrollHeight);

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
        $('.rechtsBox').css('width','0%');
    }

   /**
    *   messenger.html, bei verkleinert den Browser die Recte teil die Seite
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




    /* ************************************************************ */
    /*  Web Socket + Neue Nachriichten Senden & Life Anzeigen       */
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

// LIVE AUSGABE in messagecomponents.html Zeile: 98
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
    /*  generalmessage.html / fragment: messagefragment                 */
    /* **************************************************************** */

    function sendeMessage(meinePseudonym, freundeToken, meineToken, messageToken) {

        // Datum anlegen für die Message Jahr zwei stellig und ohne Sekunden
        var d = new Date();
        var datum = ("0" + d.getDate()).slice(-2) + "." + ("0"+(d.getMonth()+1)).slice(-2) + "." +
        d.getFullYear().toString().substr(2,2) + " " + ("0" + d.getHours()).slice(-2) + ":" +
        ("0" + d.getMinutes()).slice(-2);

        var text = $("#MESSAGETEXT").val();

        // function stompFehler(): deffiniert in messagecomponents.html: messagesfragment.html Zeile:47
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
        stompClient.send("/app/messages", {}, JSON.stringify({ 'datum': datum, 'freundetoken': freundeToken, 'meintoken': meineToken, 'messagetoken': messageToken,
                            'pseudonym': meinePseudonym, 'name': '', 'vorname':'', 'text': text, 'role': 'default' }));

        $("#MESSAGETEXT").val('');
        $("#MESSAGETEXT").rows = 1;
        // $("#conversation").scrollTop = $("#conversation").scrollHeight;
    }

