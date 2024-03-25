package com.AmericanBoutique.service;


import org.springframework.security.core.userdetails.UserDetailsService;

import com.AmericanBoutique.model.User;
import com.AmericanBoutique.repo.UserRegistrationDto;

public interface UserService extends UserDetailsService {
   User findByEmail(String email);

   User save(UserRegistrationDto registration);
   User userUpdate(User user);
}
