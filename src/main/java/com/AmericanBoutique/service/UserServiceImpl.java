package com.AmericanBoutique.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.AmericanBoutique.model.Role;
import com.AmericanBoutique.model.User;
import com.AmericanBoutique.repo.UserRegistrationDto;
import com.AmericanBoutique.repo.UserRepository;

@Service
public class UserServiceImpl implements UserService {

   private final UserRepository userRepository;
   private final BCryptPasswordEncoder passwordEncoder;

   @Autowired
   public UserServiceImpl(UserRepository userRepository,
                          BCryptPasswordEncoder passwordEncoder){
       this.userRepository = userRepository;
       this.passwordEncoder = passwordEncoder;
   }

   public User findByEmail(String email){
       return userRepository.findByEmail(email);
   }


   public User save(UserRegistrationDto registration){
       User user = new User();
       user.setFirstName(registration.getFirstName());
       user.setLastName(registration.getLastName());
       user.setEmail(registration.getEmail());
       user.setPassword(passwordEncoder.encode(registration.getPassword()));
       user.setRoles(Arrays.asList(new Role("ROLE_USER")));
       return userRepository.save(user);
   }

   public List<User> allUsers(){
       return userRepository.findAll();
   }

   @Override
    public User userUpdate(User user){
        return userRepository.save(user);
    }

   @Override
   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       User user = userRepository.findByEmail(email);
       if (user == null){
           throw new UsernameNotFoundException("Invalid Username or Password.");
       }
       return new org.springframework.security.core.userdetails.User(user.getEmail(),
               user.getPassword(),
               mapRolesToAuthorities(user.getRoles()));
   }

   private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
       return roles.stream()
               .map(role -> new SimpleGrantedAuthority(role.getName()))
               .collect(Collectors.toList());
   }
}