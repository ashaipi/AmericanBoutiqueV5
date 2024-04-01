package com.AmericanBoutique.service;

import com.AmericanBoutique.model.PlaceOrder;
import com.AmericanBoutique.repo.PlaceOrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaceOrderServiceImpl implements PlaceOrderService {

    private final UserService userService;
    private final PlaceOrderRepo placeOrderRepo;
    @Autowired
    public PlaceOrderServiceImpl(PlaceOrderRepo placeOrderRepo,
                                 UserService userService){
        this.placeOrderRepo = placeOrderRepo;
        this.userService = userService;
    }

    @Override
    public PlaceOrder saveUserInfo(PlaceOrder placeOrder){
            // Save the PlaceOrder
            return placeOrderRepo.save(placeOrder);
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
