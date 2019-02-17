package com.example.demo.service.impl;

import com.example.demo.dao.UserRepository;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //System.out.println("FIND USER: " + username);
        User domainUser = userRepository.findByUserName(username).get();
        return new org.springframework.security.core.userdetails.User(
                domainUser.getUserName(),
                "{noop}" + domainUser.getPassword(),
                true,
                true,
                true,
                true,
                getAuthorities(domainUser.getRole())
        );
    }

    public Collection<? extends GrantedAuthority> getAuthorities(Role role)
    {
        List<SimpleGrantedAuthority> authList = new ArrayList<>();
        authList.add(new SimpleGrantedAuthority(role.name()));
        return authList;
    }
}
