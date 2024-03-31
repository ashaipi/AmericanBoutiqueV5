package com.AmericanBoutique.controller;

import com.AmericanBoutique.model.Orders;
import com.AmericanBoutique.model.Product;
import com.AmericanBoutique.model.User;
import com.AmericanBoutique.service.OrderServiceImpl;
import com.AmericanBoutique.service.ProductServiceImpl;
import com.AmericanBoutique.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderController {

    private final OrderServiceImpl orderServiceImpl;
    private final ProductServiceImpl productService;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public OrderController(UserServiceImpl userServiceImpl,
                           ProductServiceImpl productService,
                           OrderServiceImpl orderServiceImpl){
        this.userServiceImpl = userServiceImpl;
        this.productService = productService;
        this.orderServiceImpl = orderServiceImpl;
    }

    //@PostMapping("/orders")
    @RequestMapping(value = "/orders", method = {RequestMethod.GET, RequestMethod.POST})
    public String getProduct(Authentication authentication){
        User user = userServiceImpl.findByEmail(authentication.getName());
        Product product = productService.getProductById(1L);

        System.out.println("------------> "+product.getProductName());
        System.out.println("------------> "+product.getPrice());
        System.out.println("------------> "+user.getFirstName());
        Orders orders = new Orders();
        orders.setUser(user);
        orders.setProduct(product);
        orders.setStatus("Completed");
        orderServiceImpl.saveOrder(orders);
        return "TEST";
    }

    //@GetMapping("/shoppingCart/{id}")
    @GetMapping("/deleOrders")
    public String deleteItemInCart() {
        Long id = 26L;
        System.out.println("-[3]---> ShoppingCartController class - deleteItemInCart() method - Endpoint(/shoppingCart/{id}) - EndPoint(redirect:/shopping_cart)");
        System.out.println("----------------> "+id);

        //Orders orders = orderServiceImpl.getOrderById(id);

//        Session session = HibernateUtil.getSessionFactory().openSession();
//        String deleteQuery = "DELETE FROM Orders WHERE id = :oId";
//        Query query = session.createNativeQuery(deleteQuery);
//        query.setParameter("oId", id);
//        session.beginTransaction();
//        int deletedCount = query.executeUpdate();
//        session.getTransaction().commit();
//        session.close();


        orderServiceImpl.deleteOrderFromCart(id);


       //orderServiceImpl.deleteOrderById(id);

        //orderServiceImpl.deleteOrderByEntity(orders);
        //orderServiceImpl.deleteOrderHibernate1ById(id);
        //orderServiceImpl.deleteOrderHibernate2ById(id);

        //orderServiceImpl.deleteJoinOrderFromCart(id);
        //shoppingCartService.deleteItemInCartById(id);
        return "TEST";
    }

    // Define endpoints for managing orders

}
