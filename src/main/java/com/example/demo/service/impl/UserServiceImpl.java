package com.example.demo.service.impl;

import com.example.demo.dao.UserRepository;
import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public Collection<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> getById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public User create(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User update(User user) {
        return userRepository.saveAndFlush(user);
    }

    public Optional<User> getByName(String username) {
        return userRepository.findByUserName(username);
    }
}
