/*
 *   den 14.08.2021
 *   richtig programieren
 *   https://www.youtube.com/watch?v=Lu-eBtYTRBY
 */

    /* Überprüfen Browserunterstützung für cookie */
    function cookieEnabled(){
        if(navigator.cookieEnabled === false ){
            document.getElementById("COOKIEBOX").style.display = "block";
     }
    }

   /**
    *	setCookie: setzt neue cookie
    *	muss zugesendet sein: Name, value und
    *	xday = in Tagen ( 5 = für 5 Tage...)
    */
    function setCookie(name, value, xday){

    	//alert(name+'/'+value+'/'+xday);
        var d = new Date();
    	d.setTime(d.getTime() + (xday * 24 * 60 * 60 * 1000));
    	var expires = "expires="+d.toUTCString();
    	document.cookie = name + "=" + value + ";" + expires + ";path=/";

    }


    /*
    *	getCookie: prüft ob mit gleichen Namen cookie vorhanden ist
    *	z.b.s getCookie('hammer'); function abrufen. Fertig
    */

    function getCookie(name)
    {
      var na = name + "=";
      var decodedCookie = decodeURIComponent(document.cookie);
      var ca = decodedCookie.split(';');
      for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
          c = c.substring(1);
        }
        if (c.indexOf(na) == 0) {
          return c.substring(na.length, c.length);
        }
      }
      return "";
    }

    // Cookie Löschen
    function deleteCookie(name)
    {
       document.cookie = name + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC;";
    }