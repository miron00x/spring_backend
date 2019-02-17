package com.example.demo.controller;

import com.example.demo.domain.Document;
import com.example.demo.domain.User;
import com.example.demo.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @GetMapping("/api/users/{username}")
    User readUser(@PathVariable String username){
        return userService.getByName(username).get();
    }

    @GetMapping("/api/user")
    User readUserByBody(@Valid @RequestBody String username, @Valid @RequestBody String password){
        return userService.getByName(username).get();
    }

    @PostMapping("/api/{username}")
    public User addUser(@PathVariable String username,
                                @Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping("/api/{username}")
    public User updateUser(@PathVariable String username,
                                   @Valid @RequestBody User userRequest) {
        return userService.update(userRequest);
    }

    @DeleteMapping("/api/{username}/{userId}")
    public void deleteUser(@PathVariable String username,
                               @PathVariable Long userId) {
        userService.delete(userId);
    }
}
