/*
 *  Den 14.02.2022
 */

     /*
      *     Zufall Fraben
      *     messenger.html Zeile: 84 (span backgronud-color, wenn kein Bild vorhanden ist)
      *     und den start von dieser functrion Zeile: 127 (setRandomColor)
      */
     function getRandomColor() {
         var letters = '123456789ABCDEF';   /* null ausgelassen */
         var color = '#';
         for (var i = 0; i < 6; i++) {
             color += letters[Math.floor(Math.random() * 15)]; /* Original 16, null ausgelassen */
         }
         return color;
    }

    function setRandomColor(id){
       $( "#color"+id ).css ( "background-color", getRandomColor());
    }