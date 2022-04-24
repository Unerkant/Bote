/*
 *  Den 17.02.2022
 *
 *  das javasccript: profil.js ist nur für den profilFragment(von settingcomponents.html)
 *  angelegt und die alle functionen werden von profilFragment gestartet...
 *
 *  die functionen: codeHolen() & codePrufen werden von
 *  mailValid, telefonValid & AcconudLoschen benutzt
 *  zugesendete Daten:
 *  1. token = meinen Token
 *  2. name = was soll updaten (mail, telefon)
 *  3. value = mail-adresse oder telefonnummer
 *
 *  die function profilSave() speichert alle geänderten Daten ins Datenbank
 *  SettingController.java  @PostMapping(value = "/profilsave")
 */


 /**
  * wird benutzt von #PROFILCODE
  * settingcomponents/ Zeile: 110
  * PROFILCODE: // fehler & ok Fenster ausbllenden, input-code leer setzen
  * #PROFILCODE: ausblenden
  *
  * #CROPPABBRECHNEN: Zeile 129
  *
  * #UPLOADABBRECHEN: Zeile 140
  */
 function profilAbbrechen(id){
    event.preventDefault();
    switch(id){
        case'PROFILCODE':       $('#CODEINFO').hide();
                                $('#CODEFEHLER').hide();
                                $('#PROFILCODE1').val('');
                                $('#PROFILCODE2').val('');
                                $('#PROFILCODE3').val('');
                                $('#PROFILCODE4').val('');
                                $('#'+id).hide();
                                break;
        case'CROPPABBRECHEN':   $('.cropp').toggle();
                                break;
        case'UPLOADABBRECHEN':  $('#BILDUPLOAD').hide();
                                $('#UPABBR').hide();
                                $('#HINZU').show();
                                break;
        default:                break;
    }

 }

  /**
   * Offnet nur das Fenster 'Dateien für Upload auswählen'
   * settingcomponents/ Zeile 138 & 142
   */
  function uploadFile(data){
     event.preventDefault();
     $('#UPLOADFILE').trigger('click');
  }

  /**
   * type: jpeg, png, gif oder heic
   * wird benutzt in settingcomponents.html on change Zeile: 184
   * ermiteln von type Bilder, bei findich einen HEIC - Fehler ausgeben
   * weil nicht jeder heic-type werde untrschtützt
   */
  function uploadType(file){

     for(var i = 0; i < file.length; i++){
         //var names   = file[i].name;
         //var bytes   = file[i].size;
         var types   = file[i].type;
     }
     var heics = types.substr(types.lastIndexOf('/') + 1);
     if(heics == 'heic'){
         profilMessage('falschetype');
     }
     return heics;
  }

 /**
  * Profil Bild Löschen
  * Zeile: 163
  */
 function profilbildLoschen(bildname){
    event.preventDefault();
    var warnung = 'sind sie sicher dass Sie ihren Profil Bild Löschen möchten';
     if(confirm(warnung) == true){
        $.post('/profilbildloschen', {'bildname': bildname})
        .done(function(out){
            if(out == 1){
                profilMessage('bildGeloscht');
            }else{
                profilMessage('nichtGeloscht');
            }
        });
     }else{
        return false;
     }
 }


 /**
  * Vorname Eintragen oder Ändern
  */
 function vornameValid(token, name, form){
    event.preventDefault();
    var valueVorname    = form[0].value;
    var valuePlaceholder= form[0]['placeholder'];
    var warnung = 'sind sie sicher dass Sie ihre Vorname ändern möchten';
    // form[1].id = Die Buttons ID werden nicht Benuztz

    /* Vorname auf Leer & gleicheits Prüfen */
    if(valueVorname.length < 2 && name == 'vorname' ){
        profilMessage('kurzevorname');
        return false;
    }else if(valueVorname == valuePlaceholder){
        return false;
    }else{
        if(confirm(warnung) == true){
            profilSave(token, name, valueVorname);
        }else{
            return false;
        }
    }
 }

 /**
  * Name Eintragen
  */
 function nameValid(token, name, form){
     event.preventDefault();
     var valueName          = form[0].value;
     var valuePlaceholder   = form[0]['placeholder'];
     var warnung = 'sind sie sicher dass Sie ihre Name ändern möchten';
     // form[1].id = Die Buttons ID werden nicht Benuztz

     /* Name auf Leer & gleiheits Prüfen */
     if(valueName.length < 2 && name == 'name'){
         profilMessage('kurzename');
         return false;
     }else if(valueName == valuePlaceholder){
        return false;
     }else{
        if(confirm(warnung) == true){
        profilSave(token, name, valueName);
        }else{
            return false;
        }
     }
 }

 /**
  * E-Mail auf richtigkeit & gleichets Prüfen
  * nach erfolgreichen prüfen senden die Dtaen an
  * function codeHolen()...token, name(mail) & input value
  */
 function mailValid(token, name, form){
    event.preventDefault();
    var valueMail   = form[0].value;
    var valuePlaceholder = form[0]['placeholder'];
    var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    var warnung = 'sind sie sicher dass Sie ihre E-Mail-Adresse ändern möchten';
    // form[1].id = Die Buttons ID werden nicht Benuztz

    /* E-Mail Prüfen */
    if(!emailPattern.test(valueMail) && name == 'mail'){
        profilMessage('falschemail');
        return false;
    }else if(valueMail == valuePlaceholder){
        profilMessage('gleichemail');
        return false;
    }else{
        if(confirm(warnung) == true){
            codeHolen(token, name, valueMail);
        }else{
            return false
        }
    }
 }

 /**
  * Telefon Prüfen
  */
 function telefonValid(token, name, form){
    event.preventDefault();
    var valueTelefon    = form[0].value;
    var valuePlaceholder= form[0]['placeholder'];
    var telefon = valueTelefon.trim().replaceAll("\\s+", "");
    var warnung = 'sind sie sicher dass Sie ihre Telefonnummer ändern möchten';
    // form[1].id = Die Buttons ID werden nicht Benuztz

     /* Telefon Prüfen*/
    if(telefon.length < 8 && name == 'telefon'){
        profilMessage('falschetelefon');
        return false;
    }else if(telefon == valuePlaceholder){
        profilMessage('gleichetelefon');
        return false;
    }else{
        if(confirm(warnung) == true){
            codeHolen(token, name, telefon);
        }else{
            return false;
        }
    }
 }

 /**
  * Freischaltcode anfordern(Code-bLock wurde eingeblendet )
  * __________________________________________________________
  * zugesendete freischaltscode(code) von SettingController/ @PostMapping(value="/codeHolen")
  * wird weiter an den Fragment: 'profilfragmnet' Zeile: 49
  * input feld hidden Zeile: 69 , zum vergleich weitergegeben(jquery: val)
  */
 function codeHolen(token, name, value){
    event.preventDefault();
    $.post('/codeHolen', {'codetoken': token, 'codename': name, 'codevalue': value})
    .done(function(code){
        //alert(code);
        if(code == 'nomail' || code == 'nosms' || code == 'nocode'){
            profilMessage(code);
        }else{
            //alert(name);
            $('#PROFILCODE').fadeIn(300);
            $('#PROFILCODE1').focus();
            $('#CODEINFO').show();
            // gespeicher in input hidden: settingcomponents.html, Zeile: 70
            $('#PROFILCODEHIDDEN').val(code);
            $('#PROFILNAMEHIDDEN').val(name);
            $('#PROFILVALUEHIDDEN').val(value);
        }

    });
 }


  /**
   *    Code prüfen von mail, telefon oder accound Löschen
   */
 var versuche = 0;
 function codePrufen(token,form){
    event.preventDefault();

    var itemCode  = form[0].value;  /* zugesendete Code  */
    var itemUpdate= form[1].value;  /* was soll Updaten (mail, telefon oder loschen) */
    var itemValue = form[2].value;  /* neues mail oder telefon */
    var code1     = form[3].value;
    var code2     = form[4].value;
    var code3     = form[5].value;
    var code4     = form[6].value;
    var total     = code1+''+code2+''+code3+''+code4;
    var inputCode = total.trim().replaceAll("\\s+", "");

    //alert(itemCode+'/'+itemUpdate+'/'+itemValue +':'+ inputCode);
    if(code1 == "" || code1 == null){
        $('#PROFILCODE1').focus();
        $('#PROFILFORM').css('border','3px solid red');
        return false;
    }else if(code2 == "" || code2 == null){
        $('#PROFILCODE2').focus();
        return false;
    }else if(code3 == "" || code3 == null){
        $('#PROFILCODE3').focus();
        return false;
    }else if(itemCode != inputCode){
        $('#PROFILCODE1').val('');
        $('#PROFILCODE2').val('');
        $('#PROFILCODE3').val('');
        $('#PROFILCODE4').val('');
        $('#PROFILCODE1').focus();
        $('#PROFILFORM').css('border','3px solid red');
        $('#CODEINFO').fadeOut(300);
        $('#CODEFEHLER').fadeIn(600);
        /* bei Falsche Code, Fehler-Info anzeigen */
        if(versuche == 0){
            $('#RICHTIGECODE').html(itemCode);
        }
        /* nach 3 versuchen abbrechen */
        if(versuche == 3){
            location.href='/setting';
        }
        versuche++;
        return false;
    }else if(itemCode == inputCode){
        // Loschen oder ändern
        if(itemUpdate == 'loschen'){
            accountLoschen(token);
        }else{
            profilSave(token, itemUpdate, itemValue);
        }
    }else{
        profilMessage('codeFehler');
    }
 }

 /**
  * Daten in Datenbank Save/Update/Delete
  */
 function profilSave(token, name, value){
    event.preventDefault();
    $.post('/profilsave', {'tokensave': token, 'namesave': name, 'valuesave': value })
    .done(function(response){
         //alert(response);
        //$('#SETTINGOK').replaceWith(data.output);
        profilAbbrechen('PROFILCODE');
        profilMessage(response);
    });
 }



 /**
  * Abmelden
  * nur cookie gelöscht, Daten in den Datanbank werden alle erhalten
  * für spähtere anmeldung unter den gleiche E-Mail-Adresse oder Telefonnummer
  *
  * Update: Tabelle session spalte 'letztenoutlog'
  * SettingController/ @PostMapping(value = "/accountabmelden")
  */
 function abmeldenSave(token, name, platzhalter ){
    event.preventDefault();
    if(confirm('Sind Sie sicher, dass Sie sich abmelden möchten?') == true){

        $.post('/accountabmelden', {'abmeldentoken': token})
        .done(function(data){
            if(data == 'abgemeldet'){
                //alert(data);          //ZURZEIT NICHT BENUTZT
            }
        });
        deleteCookie('userid');
        if(document.cookie.indexOf('userid') == -1){
            profilMessage('abmelden');
        }else{
            profilMessage('noabmelden');
        }
    }else{
        return false;
    }
 }

 /**
  * Accound Löschen
  * Alle Daten, aus allen Tabellen in den Datenbank + cookie werden unwiderruflich gelöscht
  * Achtung: die cookie werden nur nach den erfolgreichen Datenbakn löschung gelöscht
  * in denn function profilMessage()/accountLoschen
  */
 function accountValid(token, name, platzhalter){
    event.preventDefault();
    var warnung = 'Sind Sie sich absolut sicher, dass Sie Ihr Bote-Konto löschen möchten,'
                    + 'klicken Sie auf die Schaltfläche OK.\n\n'
                    + 'Bitte beachten Sie, dass Sie die Daten nicht mehr wiederherstellen können.\n';
    if(confirm(warnung) == true){
            codeHolen(token, name, platzhalter);
    }else{
        return false;
    }

 }

 /**
  * Accound Löschen
  * den Token werde von function: codePrufen, Zeile: 217(hier oben) zugesendet
  *
  * nach erfolgreichen Account Löschen wird einen Fragment angezeigt
  * components/<div data-th-fragment="accountloschen"> Zeile: 191
  */
 function accountLoschen(token){
    event.preventDefault();
    //alert(token +'/'+name +'/'+ value);
    $.post('/accountloschen', {'token': token})
    .done(function(data){
        //alert(data);
        $('#ACCOUNTLOSCHENFRAGMENT').replaceWith(data);
        $('#PROFILCODE').hide();
        $('#ACCOUNTLOSCHEN').show();
        //profilMessage(data);
    });
 }


 /**
  * Profil Fehler ausgeben
  */
 function profilMessage(message){
    event.preventDefault();
   // alert(message);
    switch(message){
        case'noBild':       $('#SETTINGFEHLER').fadeIn(600).delay(10000).fadeOut(600).queue();
                            $('#SETTINGFEHLER').html('Bild kann nicht gespeichert werden.');
                            break;
        case'falschetype':  $('#SETTINGFEHLER').fadeIn(600).delay(10000).fadeOut(600).queue();
                            $('#SETTINGFEHLER').html('bitte, HEIC Bilder in jpg oder png umwandeln');
                            break;
        case'nichtGeloscht':$('#SETTINGFEHLER').fadeIn(600).delay(10000).fadeOut(600).queue();
                            $('#SETTINGFEHLER').html('Bild kann nicht Gelöscht werden.');
                            break;
        case'kurzevorname': $('#SETTINGFEHLER').fadeIn(600).delay(10000).fadeOut(600).queue();
                            $('#SETTINGFEHLER').html('Vorname besteht nicht mindestens aus 2 Zeichen.');
                            break;
        case'kurzename':    $('#SETTINGFEHLER').fadeIn(600).delay(10000).fadeOut(600).queue();
                            $('#SETTINGFEHLER').html('Name besteht nicht mindestens aus 2 Zeichen.');
                            break;
        case'falschemail':  $('#SETTINGFEHLER').fadeIn(600).delay(10000).fadeOut(600).queue();
                            $('#SETTINGFEHLER').html('Bitte geben Sie eine gültige E-Mail Adresse ein.');
                            break;
        case'gleichemail':  $('#SETTINGFEHLER').fadeIn(600).delay(10000).fadeOut(600).queue();
                            $('#SETTINGFEHLER').html('Bereits mit dieser E-Mail-Adresse verknüpft.');
                            break;
        case'falschetelefon':$('#SETTINGFEHLER').fadeIn(600).delay(10000).fadeOut(600).queue();
                            $('#SETTINGFEHLER').html('Bitte geben Sie eine gültige Telefonnummer.');
                            break;
        case'gleichetelefon':$('#SETTINGFEHLER').fadeIn(600).delay(10000).fadeOut(600).queue();
                            $('#SETTINGFEHLER').html('Bereits mit diesem Telefonnummer verknüpft.');
                            break;
        case'nomail':       $('#SETTINGFEHLER').fadeIn(600).delay(10000).fadeOut(600).queue();
                            $('#SETTINGFEHLER').html('E-Mail kann nicht gesendet werden');
                            break;
        case'nosms':        $('#SETTINGFEHLER').fadeIn(600).delay(10000).fadeOut(600).queue();
                            $('#SETTINGFEHLER').html('Keine SMS vesendet.');
                            break;
        case'codeFehler':   $('#PROFILCODE').hide();
                            $('#SETTINGFEHLER').fadeIn(600).delay(10000).fadeOut(600).queue();
                            $('#SETTINGFEHLER').html('ERROR: fuction datenSave().');
                            break;
        case'noabmelden':   $('#SETTINGFEHLER').fadeIn(600).delay(10000).fadeOut(600).queue();
                            $('#SETTINGFEHLER').html('Fehler: Sie sind nicht abgemeldet.');
                             break;
        case'profilerror':  $('#SETTINGFEHLER').fadeIn(600).delay(10000).fadeOut(600).queue();
                            $('#SETTINGFEHLER').html('Daten werden nicht gespeichert.');
                            break;

        case'okBild':       $('#SETTINGOK').fadeIn(600).delay(10000).fadeOut(600);
                            $('#SETTINGOK').html('Ihre Profil Bild wurde erfolgreich gespeichert');
                            break;
        case'bildGeloscht': $('#SETTINGOK').fadeIn(600).delay(10000).fadeOut(600);
                            $('#SETTINGOK').html('Ihre Profil Bild wurde erfolgreich gelöscht');
                            setTimeout(function(){location.href='/'}, 5000);
                            break;
        case'abmelden':     $('#SETTINGOK').fadeIn(600).delay(10000).fadeOut(600);
                            $('#SETTINGOK').html('Sie haben sich erfogreich abgemeldet.<br>'
                            + '<small>mit Alten Daten können sich jederzeit wieder anmelden.</small>');
                            setTimeout(function(){location.href='/'}, 5000);
                            break;
        case'vorname':      $('#SETTINGOK').fadeIn(600).delay(10000).fadeOut(600);
                            $('#SETTINGOK').html('Ihre Vorname wurde erfolgreich gespeichert');
                            break;
        case'name':         $('#SETTINGOK').fadeIn(600).delay(10000).fadeOut(600);
                            $('#SETTINGOK').html('Ihre Name wurde erfolgreich gespeichert');
                            break;
        case'mail':         $('#PROFILCODE').hide();
                            $('#SETTINGOK').fadeIn(600).delay(10000).fadeOut(600);
                            $('#SETTINGOK').html('Ihre E-Mail wurde erfolgreich geändert');
                            break;
        case'telefon':      $('#SETTINGOK').fadeIn(600).delay(10000).fadeOut(600);
                            $('#SETTINGOK').html('Ihre Telefonnummer wurde erfolgreich geändert');
                            break;
        case'geloscht':     $('#SETTINGOK').fadeIn(600).delay(10000).fadeOut(600);
                            $('#SETTINGOK').html('Ihr Account ist unwiderruflich gelöscht.');
                            setTimeout(function(){location.href='/'}, 5000);
                         break;
        default:            $('#SETTINGFEHLER').fadeIn(600).delay(10000).fadeOut(600).queue();
                            $('#SETTINGFEHLER').html('ERROR: Unbekanten Fehler.');
                            break;
    }

 }
