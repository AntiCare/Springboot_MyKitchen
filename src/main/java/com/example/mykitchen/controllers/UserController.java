package com.example.mykitchen.controllers;

import com.example.mykitchen.model.UserLogin;
import com.example.mykitchen.services.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/kitchen")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class UserController {
    private UserService us ;

    public UserController(UserService us){
        this.us = us;
    }

    /**
     * POST -login, check username & password
     * @param Uname username
     * @param Upassword password
     * @return HTTP status 200 or 404
     */
    @PostMapping("/login")
    public Object login(@RequestParam String Uname, @RequestParam String Upassword){
        return us.login(Uname,Upassword);
    }

    /**
     * POST -register, add user to database.
     * @param user register user
     * @return HTTP status 201 or 405
     */
    @PostMapping(consumes = {"application/json"})
    public Object registerJson(@RequestBody UserLogin user){
        return us.saveUser(user);
    }
}
