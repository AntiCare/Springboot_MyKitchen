package com.example.mykitchen.services;


import com.example.mykitchen.model.UserLogin;
import com.example.mykitchen.repositories.UserRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /**
     * Login, check login information
     * @param username username
     * @param password password
     * @return HTTP status 200 or 404
     */
    public Object login(String username, String password){
        List<UserLogin> users = userRepository.findAll();
        if (username != null && password!= null) {
            for (UserLogin u:users) {
                if(u.getName().equals(username) && u.getPassword().equals(password)){
                    return  HttpServletResponse.SC_OK;
                }
            }
        }
        return HttpServletResponse.SC_NOT_FOUND;
    }

    /**
     * Register, add user to database.
     * @param u user data
     * @return HTTP status 201 or 405
     */
    public Object saveUser(UserLogin u){
        try {
            userRepository.save(u);
        }catch (Exception e){
            return HttpServletResponse.SC_METHOD_NOT_ALLOWED;
        }
        return  HttpServletResponse.SC_CREATED;
    }
}
