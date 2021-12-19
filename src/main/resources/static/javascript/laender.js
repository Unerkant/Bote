   /*
    *   den 20.10.2021
    *
    */


   /**
    *   wird gestartet in telefonlogin.html: Zeile 58
    *   und in den Länder-fragments: Zeile 142
    */
function lender(id){
    event.preventDefault();
    //alert(val);
    var x = document.getElementById(id);
    if(x.style.display === 'block'){
            x.style.display = 'none';
        }else{
            x.style.display = 'block';
        }
}

   /**
    *   Länder Laden
    *   wird gestarted von components.html(Länder Vorwahl)
    *   Zeile: 160
    */
function lenderLaden(stadt, vorwahl, flagge){
    event.preventDefault();

    var landname = document.getElementById('LANDNAME');
    var landvorwahl = document.getElementById('LANDVORWAHL');
    var landflagge = document.getElementById('LANDFLAGGE');

    landflagge.src = '/flagge/'+flagge+'.png';
    landname.innerHTML = stadt;
    landvorwahl.value = '+ '+vorwahl;
    lender('LAENDER');
}


   /**
    *   function search zurzeit funktioniert nicht
    *
    *   wird benutzt von templates/fragments/components.html
    *   Teil von Länder Vorwahl
    */

function search(selector, text){
    event.preventDefault();

    var output = document.getElementById('output');
    var element = document.querySelectorAll(selector);
    output.innerHTML = element.length + " / " +text;

}
