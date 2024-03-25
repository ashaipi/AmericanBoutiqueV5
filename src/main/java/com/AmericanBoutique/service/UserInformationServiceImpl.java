package com.AmericanBoutique.service;

import com.AmericanBoutique.model.Product;
import com.AmericanBoutique.model.User;
import com.AmericanBoutique.model.UserInformation;
import com.AmericanBoutique.repo.ProductRepository;
import com.AmericanBoutique.repo.UserInformationRepo;
import com.AmericanBoutique.repo.UserRegistrationDto;
import com.AmericanBoutique.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInformationServiceImpl implements UserInformationService {

    @Autowired
    private UserService userService;

    private UserRegistrationDto userRegistrationDto;

    @Autowired
    private UserInformationRepo userInformationRepo;

    public UserInformationServiceImpl(UserInformationRepo userInformationRepo){
        this.userInformationRepo = userInformationRepo;
    }

    @Override
    public UserInformation saveUserInfo(UserInformation userInformation, Authentication authentication){

        // Get the email of the authenticated user
        String userEmail = authentication.getName();

        // Find the user by email
        User existingUser  = userService.findByEmail(userEmail);

            userInformation.setUser(existingUser);

            // Save the UserInformation
            return userInformationRepo.save(userInformation);

        //return userInformationRepo.save(userInformation);
    }

    @Override
    public UserInformation getUserInfoById(Long id){
        return userInformationRepo.findById(id).get();
    }

    @Override
    public UserInformation updateUserInfo(UserInformation  userInformation){
        return userInformationRepo.save(userInformation);
    }

    @Override
    public void deleteUserInfoById(Long id){
        userInformationRepo.deleteById(id);
    }

}
