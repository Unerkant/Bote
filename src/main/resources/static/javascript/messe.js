/*
*   Den 13.12.2021
*/

   /**
    *   bei Mobil version Message Seite(Rechts) offnen & schließen
    */
function openBox(){
    event.preventDefault();
    if(window.innerWidth < 651){
        document.getElementById("MESSAGEBOX").style.width = "100%";
    }else{
        return;
        //alert('openElse');
     }
}
function closeBox(){
    event.preventDefault();
    if(window.innerWidth < 651){
        document.getElementById("MESSAGEBOX").style.width = "0%";
    }else{
        return;
    }
}