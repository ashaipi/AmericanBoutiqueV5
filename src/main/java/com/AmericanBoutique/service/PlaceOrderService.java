package com.AmericanBoutique.service;

import com.AmericanBoutique.model.PlaceOrder;
import org.springframework.security.core.Authentication;

public interface PlaceOrderService {
    PlaceOrder saveUserInfo(PlaceOrder placeOrder);
    PlaceOrder getUserInfoById(Long id);
    PlaceOrder updateUserInfo(PlaceOrder placeOrder);
    void deleteUserInfoById(Long id);
}
