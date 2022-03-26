package Bote.controller;

import Bote.model.User;
import Bote.service.UserService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MailController {

    private User meineDaten;
    @Autowired
    private UserService userService;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @SneakyThrows
    @GetMapping(value = "/login/maillogin")
    public String login(@CookieValue(value = "userid", required = false) Long userId){

        meineDaten = userService.findeUserToken(userId);
        logger.info("MailController GetMapping: /login/maillogin ..." + meineDaten);
        return (meineDaten == null ? "/login/maillogin" : "/messenger");
    }
}
