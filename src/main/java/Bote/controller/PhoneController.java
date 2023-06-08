package Bote.controller;

import Bote.service.FreundeService;
import Bote.service.PhoneService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * bearbeitet am 5.06.2023
 */

@Controller
public class PhoneController {

    @Autowired
    private FreundeService freundeService;
    @Autowired
    private PhoneService phoneService;

    private List    telefonate;
    private List    phoneFreunde;
    private String  myToken;

    @GetMapping(value = "/phone")
    public String phone(@CookieValue(value = "userid", required = false) String phonecookie,
                        HttpServletRequest request, Model model){


        telefonate = phoneService.telefonatAusgeben(phonecookie);
        model.addAttribute("telefonaten", telefonate);
        model.addAttribute("phoneRequestUri", request.getRequestURI());

        //System.out.println("PhoneController/GetMapping/Phone: " + phonecookie);
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

        //System.out.println("PhoneController/PostMapping/anrufdelete: " + myToken +'/'+ deleteId);

        return (deletecookie == null ? "/login/maillogin" : "/phone :: #OK"+deleteId);
    }
}
