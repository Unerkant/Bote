package Bote.controller;

import Bote.model.User;
import Bote.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MailController {

    private User meineDaten;
    @Autowired
    private UserService userService;

    @SneakyThrows
    @GetMapping(value = "/login/maillogin")
    public String login(@CookieValue(value = "userid", required = false) Long userId){

        meineDaten = userService.findeUserToken(userId);
        return (meineDaten == null ? "/login/maillogin" : "/messenger");
    }
}
