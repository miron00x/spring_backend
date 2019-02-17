package com.example.demo.service;

import com.example.demo.domain.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {
    Collection<User> getAll();
    void delete(long id);
    Optional<User> getById(long id);
    Optional<User> getByName(String name);
    User create(User user);
    User update(User user);
}
