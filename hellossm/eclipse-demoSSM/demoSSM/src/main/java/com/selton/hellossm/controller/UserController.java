package com.selton.hellossm.controller;

import com.selton.hellossm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("userLogin")
    public String userLogin(String name,String password){


        if (userService.loginUserStatus(name,password)) {

            return "/loginsuccess.html";
        }

        return "/loginfailed.html";
    }
}
