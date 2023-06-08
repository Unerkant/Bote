package Bote.controller;

import Bote.model.Usern;
import Bote.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * bearbeitet am 5.06.2023
 */

@Controller
public class MailController {

    private Usern meineDaten;
    @Autowired
    private UserService userService;

    @GetMapping(value = "/login/maillogin")
    public String login(@CookieValue(value = "userid", required = false) String userId){

        meineDaten = userService.meineDatenHolen(userId);

        //System.out.println("MailController GetMapping: /login/maillogin ..." + meineDaten);

        return (meineDaten == null ? "/login/maillogin" : "/messenger");
    }
}
