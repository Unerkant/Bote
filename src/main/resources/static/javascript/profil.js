/*
 *  Den 17.02.2022
 */

 function bildUpload(){
    event.preventDefault();
    alert('Bile Upload');
 }

 /*
  * Vorname Eintragen
  */
 function vornameSave(token, name, form){
    event.preventDefault();
    var fehler          = null;
    var valueVorname    = form[0].value;
    // form[1].id, form[3].id = Die Buttons ID werden nicht Benuztz

    /* Vorname auf Leer Prüfen */
    if(valueVorname.length < 2 && name == 'vorname' ){
        fehler = 'vorname';
        profilFehler(fehler);
        return false;
    }else{
        profilSave(token, name, valueVorname);
    }
 }

 /*
  * Name Eintragen
  */
 function nameSave(token, name, form){
     event.preventDefault();
     var fehler     = null;
     var valueName  = form[2].value;
     // form[1].id, form[3].id = Die Buttons ID werden nicht Benuztz

     /* Name auf Leer Prüfen */
     if(valueName.length < 2 && name == 'name'){
         fehler = 'name';
         profilFehler(fehler);
         return false;
     }else{
        profilSave(token, name, valueName);
     }
 }

 /*
  * E-Mail Prüfen
  */
 function mailSave(token, name, form){
    event.preventDefault();
    var fehler      = null;
    var valueMail   = form[0].value;
    var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    // form[1].id, form[3].id = Die Buttons ID werden nicht Benuztz

    /* E-Mail Prüfen */
    if(!emailPattern.test(valueMail) && name == 'mail'){
        fehler = 'mail';
        profilFehler(fehler);
        return false;
    }else{
        profilSave(token, name, valueMail);
    }
 }

 /*
  * Telefon Prüfen
  */
 function telefonSave(token, name, form){
    event.preventDefault();
    var fehler          = null;
    var valueTelefon    = form[2].value;
    // form[1].id, form[3].id = Die Buttons ID werden nicht Benuztz

     /* Telefon Prüfen*/
    if(valueTelefon.length < 8 && name == 'telefon'){
        fehler = 'telefon';
        profilFehler(fehler);
        return false;
    }else{
        telefon = valueTelefon.trim().replaceAll("\\s+", "");
        profilSave(token, name, telefon);
    }
 }

 /*
  * Abmelden
  */
 function abmeldenSave(token, name, platzhalter ){
    event.preventDefault();
    if(confirm('Sind Sie sicher, dass Sie sich abmelden möchten?') == true){
        profilSave(token, name, platzhalter );
    }else{
        return false;
        //profilFehler('error');
    }
 }

 /*
  * Accound Löschen
  */
 function accoundLoschen(token, name, platzhalter){
    event.preventDefault();
        var text = 'Sind Sie sich absolut sicher, dass Sie Ihr Bote-Konto löschen möchten,'
                    + 'klicken Sie auf die Schaltfläche OK.\n\n'
                    + 'Bitte beachten Sie, dass Sie die Daten nicht mehr wiederherstellen können.';
        if(window.confirm(text) == true){
            profilSave(token, name, platzhalter );
        }else{
            return false;
            //profilFehler('error');
        }
 }

 /*
  * Daten in Datenbank Speichern/Ändern
  */
 function profilSave(token, name, value){
    event.preventDefault();
    $.post('/profilsave', {'tokensave':token, 'namesave':name, 'valuesave':value })
     .done(function(response){
        //alert(response);
        //$('#SETTINGOK').replaceWith(data.output);
        if(response == 'nichtgespeichert'){
            profilFehler('error');
        }else{
            profilOk(response);
        }
     });
 }


 /*
  * Profil Fehler ausgeben
  */
 function profilFehler(fehler){
    event.preventDefault();

    switch(fehler){
        case'vorname':  $('#SETTINGFEHLER').fadeIn(600).delay(2000).fadeOut(600).queue();
                        $('#SETTINGFEHLER').html('Vorname besteht nicht mindestens aus 2 Zeichen.');
                        break;
        case'name':     $('#SETTINGFEHLER').fadeIn(600).delay(2000).fadeOut(600).queue();
                        $('#SETTINGFEHLER').html('Name besteht nicht mindestens aus 2 Zeichen.');
                        break;
        case'mail':     $('#SETTINGFEHLER').fadeIn(600).delay(2000).fadeOut(600).queue();
                        $('#SETTINGFEHLER').html('Bitte geben Sie eine gültige E-Mail Adresse ein.');
                        break;
        case'telefon':  $('#SETTINGFEHLER').fadeIn(600).delay(2000).fadeOut(600).queue();
                        $('#SETTINGFEHLER').html('Bitte geben Sie eine gültige Telefonnummer.');
                        break;
        case'error':    $('#SETTINGFEHLER').fadeIn(600).delay(2000).fadeOut(600).queue();
                        $('#SETTINGFEHLER').html('Daten werden nicht gespeichert.');
                        break;
        default:        $('#SETTINGFEHLER').fadeIn(600).delay(2000).fadeOut(600).queue();
                        $('#SETTINGFEHLER').html('ERROR: Unbekanten Fehler.');
                        break;
    }
 }

 /*
  * Profil OK Ausgeben
  */
 function profilOk(ok){
    event.preventDefault();

    switch(ok){
        case'bild':     $('#SETTINGOK').fadeIn(600).delay(3000).fadeOut(600);
                        $('#SETTINGOK').html('Ihren ProfilBild wurde erfolgreich geändert');
                        break;
        case'vorname':  $('#SETTINGOK').fadeIn(600).delay(3000).fadeOut(600);
                        $('#SETTINGOK').html('Ihre Vorname wurde erfolgreich gespeichert');
                        break;
        case'name':     $('#SETTINGOK').fadeIn(600).delay(3000).fadeOut(600);
                        $('#SETTINGOK').html('Ihre Name wurde erfolgreich gespeichert');
                        break;
        case'mail':     $('#SETTINGOK').fadeIn(600).delay(3000).fadeOut(600);
                        $('#SETTINGOK').html('Ihre E-Mail wurde erfolgreich geändert');
                        break;
        case'telefon':  $('#SETTINGOK').fadeIn(600).delay(3000).fadeOut(600);
                        $('#SETTINGOK').html('Ihre Telefonnummer wurde erfolgreich geändert');
                        break;
        case'abmelden': $('#SETTINGOK').fadeIn(600).delay(3000).fadeOut(600);
                        $('#SETTINGOK').html('Sie haben sich erfogreich abgemeldet.');
                        setTimeout(function(){location.href='/'}, 3000);
                        break;
        case'loschen':  $('#SETTINGOK').fadeIn(600).delay(3000).fadeOut(600);
                         $('#SETTINGOK').html('Ihr Account ist unwiderruflich gelöscht.');
                         setTimeout(function(){location.href='/'}, 3000);
                         break;
        default:        $('#SETTINGOK').fadeIn(600).delay(3000).fadeOut(600);
                        $('#SETTINGOK').html('Default: profil.js Zeile: 165');
                        break;
    }
 }
