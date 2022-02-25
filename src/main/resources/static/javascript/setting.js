/*
 *  Den 24.12.2021
 */


   /**
    *   Function Starten
    */
$('.einstellung').click(function(){
     event.preventDefault();
     var itemId         = $(this).attr('id');
     var fragmentTitel  = $(this).attr('titel');
     var fragmentName   = $(this).attr('name');
     var myToken        = $(this).attr('token');
   //alert(itemId +'/'+ fragmentTitel +'/'+ fragmentName +'/'+ myToken );

   /**
    *   Background - Active Fenster zurück setzen
    *   gleich Active position hover setzen
    */
    var activ =  document.querySelectorAll('.einstellung');
    for( var i = 0; i < activ.length; i++){
       activ[i].style.background = 'transparent';
       activ[i].style.color = '#000';
    }
    $(this).css({"background-color":"#80CEFF", "background":"linear-gradient(to right, #66C4FF, #80CEFF)", "color":"#FFF"});

    // Rechtes Box: width 100%
    if(window.innerWidth < 651){
        document.getElementById("RECHTSBOX").style.width = "100%";
    }

   /**
    *   Daten Senden an SettingController.java
    */
 $.post('/einstellung', {'fragmentName': fragmentName, 'fragmentTitel': fragmentTitel, 'myToken': myToken})
            .done(function(data){
                      //alert(data);
                      $('#FRAGMENTANZEIGEN').replaceWith(data);
            /**
             *  data: ist einen SeitenQuellText von fragments/settingcomponents/${verschiedene}
             *  zugesendet von SettingController.java @PostMapping mit return "/setting :: #FRAGMENTANZEIGEN";
             *  den SeitenQuellText wird in setting.html #FRAGMENTANZEIGE eingeblendet
             */
        });

});


   /**
    *    Seite reload, wenn über 650px breit ist
    */
function settingZuruck(){
    event.preventDefault();
    if(window.innerWidth < 651){
        document.getElementById("RECHTSBOX").style.width = "0%";
    }else{
        location.reload();
    }
}