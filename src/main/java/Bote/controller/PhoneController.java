package Bote.controller;

import Bote.service.FreundeService;
import Bote.service.PhoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class PhoneController {

    private List    telefonate;
    private List    phoneFreunde;

    private String  myToken;


    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private FreundeService freundeService;
    @Autowired
    private PhoneService phoneService;

    @GetMapping(value = "/phone")
    public String phone(@CookieValue(value = "userid", required = false) String phonecookie,
                        HttpServletRequest request, Model model){


        telefonate = phoneService.telefonatAusgeben(phonecookie);
        model.addAttribute("telefonaten", telefonate);

        logger.info("PhoneController/GetMapping/Phone: " + phonecookie);
        return (phonecookie == null ? "/login/maillogin" : "/phone");
    }

    /**
     *  Anruf Delete
     */
    @PostMapping(value = "/anrufdelete")
    public String anrufDelete(@CookieValue(value = "userid", required = false) String deletecookie,
                              @RequestParam(value = "anrufId", required = false) Long deleteId,
                              @RequestParam(value = "myToken", required = false) String myToken,
                              HttpServletRequest request, Model model){


        phoneService.telefonatLoschen(deleteId);

        logger.info("PhoneController/PostMapping/anrufdelete: " + myToken +'/'+ deleteId);
        return (deletecookie == null ? "/login/maillogin" : "/phone :: #OK"+deleteId);
    }
}
