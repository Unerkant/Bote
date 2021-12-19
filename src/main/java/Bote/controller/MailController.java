package Bote.controller;

import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

public class MailController {

    @SneakyThrows
    @GetMapping(value = "/login/maillogin")
    public String login(@CookieValue(value = "userid", required = false) String userId){

        return (userId == null ? "/login/maillogin" : "/messenger");
    }
}
