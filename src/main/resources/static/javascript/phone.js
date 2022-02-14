/*
 *  Den 2.01.2022
 */


   /**
    *   Lösch-Button Anzeigen & verstecken
    */
 function anrufBearbeiten(){
    event.preventDefault();
    var item =  document.querySelectorAll('.anrufRemove');
        for( var i = 0; i < item.length; i++){
            $(item[i]).toggle();
        }
 }

   /**
    *   Telefonat Löschen(einzeln)
    */
 function anrufDelete(id, token){
    event.preventDefault();
   // alert(id +'/'+ token);
    $.post('/anrufdelete', {'anrufId': id, 'myToken': token})
        .done(function(data){
            //alert('#OK'+id);
           //$('#OK'+id).replaceWith(data);
           $('#OK'+id).slideDown(600).delay(5000).slideUp(600);
           $('#'+id).delay(5600).slideUp(600);
    });
 }


   /**
    *   Simple Kamera Zugriff
    */
 function cameraEinstellung(){
    event.preventDefault();
    $('#PHONEKAMERA').show();

    const video = document.querySelector('video');
    const constraints = {
        video: true,
        audio: false,
        width: { min: 200, ideal: 400, max: 500 },
        height: { min: 200, ideal: 400, max: 500 }
    };

        if ('mediaDevices' in navigator && navigator.mediaDevices.getUserMedia) {

            navigator.mediaDevices
            .getUserMedia(constraints)
            .then(stream => {
                video.srcObject = stream;
            });
       }else{
            console.error('Oops...', error);
       }
 }

   /*
    *   Kamera Ausschalten
    */
 function cameraEinstellungClose(){
    event.preventDefault();
    $('#PHONEKAMERA').hide();

    const video = document.querySelector('video');
    const stream = video.srcObject;
    const tracks = stream.getTracks();

      tracks.forEach(function(track) {
        track.stop();
      });
      video.srcObject = null;
 }