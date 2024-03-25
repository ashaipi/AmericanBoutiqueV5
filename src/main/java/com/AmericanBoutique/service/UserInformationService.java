package com.AmericanBoutique.service;

import com.AmericanBoutique.model.Product;
import com.AmericanBoutique.model.UserInformation;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserInformationService {
    UserInformation saveUserInfo(UserInformation userInformation, Authentication authentication);
    UserInformation getUserInfoById(Long id);
    UserInformation updateUserInfo(UserInformation  userInformation);
    void deleteUserInfoById(Long id);
}
