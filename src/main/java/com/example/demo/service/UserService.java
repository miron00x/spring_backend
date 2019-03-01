package com.example.demo.service;

import com.example.demo.domain.User;

import java.util.Collection;

public interface UserService {
    Collection<User> getAll();
    void delete(long id);
    User getById(long id);
    User getByName(String name);
    User create(User user);
    User update(User user);
}
