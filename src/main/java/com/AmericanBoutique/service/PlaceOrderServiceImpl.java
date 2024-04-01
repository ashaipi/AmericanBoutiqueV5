package com.AmericanBoutique.service;

import com.AmericanBoutique.model.PlaceOrder;
import com.AmericanBoutique.model.User;
import com.AmericanBoutique.repo.PlaceOrderRepo;
import com.AmericanBoutique.repo.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class PlaceOrderServiceImpl implements PlaceOrderService {

    private final UserService userService;
    private UserRegistrationDto userRegistrationDto;

    private final PlaceOrderRepo placeOrderRepo;
    @Autowired
    public PlaceOrderServiceImpl(PlaceOrderRepo placeOrderRepo,
                                 UserService userService){
        this.placeOrderRepo = placeOrderRepo;
        this.userService = userService;
    }

    @Override
    public PlaceOrder saveUserInfo(PlaceOrder placeOrder, Authentication authentication){

        // Get the email of the authenticated user
        String userEmail = authentication.getName();

        // Find the user by email
        User existingUser  = userService.findByEmail(userEmail);

            placeOrder.setUser(existingUser);

            // Save the PlaceOrder
            return placeOrderRepo.save(placeOrder);

        //return userInformationRepo.save(placeOrder);
    }

    @Override
    public PlaceOrder getUserInfoById(Long id){
        return placeOrderRepo.findById(id).get();
    }

    @Override
    public PlaceOrder updateUserInfo(PlaceOrder placeOrder){
        return placeOrderRepo.save(placeOrder);
    }

    @Override
    public void deleteUserInfoById(Long id){
        placeOrderRepo.deleteById(id);
    }

}
