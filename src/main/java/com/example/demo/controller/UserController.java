package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserServiceImpl userService;

    private static final String GET_BY_NAME = "/{username}";
    private static final String CREATE = "/{username}";
    private static final String UPD = "/{username}";
    private static final String DELETE_BY_ID = "/{userId}";

    @GetMapping(GET_BY_NAME)
    public User readUser(@PathVariable String username){
        return userService.getByName(username);
    }

    @PostMapping(CREATE)
    public User addUser(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping(UPD)
    public User updateUser(@Valid @RequestBody User userRequest) {
        return userService.update(userRequest);
    }

    @DeleteMapping(DELETE_BY_ID)
    public void deleteUser(@PathVariable Long userId) {
        userService.delete(userId);
    }
}
