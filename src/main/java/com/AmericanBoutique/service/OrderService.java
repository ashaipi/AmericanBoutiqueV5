package com.AmericanBoutique.service;

import com.AmericanBoutique.model.Orders;

import java.util.List;

public interface OrderService {

    List<Orders> getAllOrders();
    Orders saveOrder(Orders orders);
    Orders getOrderById(Long id);
    void deleteOrderById(Long id);

    void deleteOrderByEntity(Orders orders);

    void deleteOrderHibernate1ById(Long id);
    void deleteOrderHibernate2ById(Long id);

//
//    Orders getOrderByUserId(Long user_id);
}
