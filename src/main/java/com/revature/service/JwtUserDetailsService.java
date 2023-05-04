package com.revature.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

// This class is used to authenticate users with our database
@Service
public class JwtUserDetailsService implements UserDetailsService {
    // Here's a dummy implementation of load by username
    // In the real world, we would want to get this user by their username, using some sort of DAO
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.equals("roryeiffe")) {
            // we use bcrypt to hash the password:
            // https://www.javainuse.com/onlineBcrypt
            return new User("roryeiffe", "$2a$12$VU5137Ujeo8nMx6Bb2VZFOjsG/uV6cTJ/Cl1QpcSX0aQkF1D//bBS", new ArrayList<>());
        }
        else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

}
