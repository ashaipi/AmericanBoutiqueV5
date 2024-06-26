package com.AmericanBoutique.controller;

import com.AmericanBoutique.model.Orders;
import com.AmericanBoutique.model.Product;
import com.AmericanBoutique.model.User;
import com.AmericanBoutique.service.*;
import org.bouncycastle.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@Controller
public class ShoppingCartController {

    Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

    private final UserServiceImpl userServiceImpl;
    private final OrderServiceImpl orderServiceImpl;
    private final ProductService productService;

    @Autowired
    public ShoppingCartController(OrderServiceImpl orderServiceImpl,
                                  ProductService productService,
                                  UserServiceImpl userServiceImpl) {
        this.orderServiceImpl = orderServiceImpl;
        this.productService = productService;
        this.userServiceImpl = userServiceImpl;
    }

    // add chosen product to shopping bag
    // Define endpoints for managing shopping cart
    @RequestMapping(value = "/addToCart", method = {RequestMethod.GET, RequestMethod.POST})
    @Transactional
    public String addToCart(@RequestParam(name = "id") Long productId,
                            Authentication authentication) {
        System.out.println("-[1]---> ShoppingCartController class - addToCart() method - Endpoint(/addToCart) - HTML(redirect:/home)");

        Product product = productService.getProductById(productId);

        // Call the CartService to save the product details to the cart table
        // shoppingCartService.addToCart(p.getProductName(), p.getDescription(), p.getPrice(), p.getImg());

        // find user id
        User user = userServiceImpl.findByEmail(authentication.getName());
        System.out.println("------------> User Name: "+user.getFirstName());
        Orders orders = new Orders();
        orders.setUser(user);
        orders.setProduct(product);
        orders.setStatus("Completed");
        try {
            orderServiceImpl.saveOrder(orders);
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            //e.printStackTrace();
            logger.error("ShoppingCartController class - addToCart method: An error occurred:", e);
        }

        // Redirect the user to a confirmation page or back to the product page
        return "redirect:/home";
    }

    // List all order products that in a shopping bag
    @GetMapping("/shoppingCart")
    public String listItems(Model model, Authentication authentication) {
        System.out.println("-[2]-----> ShoppingCartController class - listItems() method - Endpoint(/shoppingCart) - HTML(shopping_cart)");

        // find login user id
        User user = userServiceImpl.findByEmail(authentication.getName());
        System.out.println("------------> User Name: "+user.getFirstName());

        // Find the total in a shopping bag for all users
        List<Orders> allOrdersInBag = orderServiceImpl.getAllOrders();
        System.out.println("#[2.1]##############> Size of orderList: "+allOrdersInBag.size());

        // Create a new Order object to save current user orders in a shopping bag
        List<Orders> ordersList = new ArrayList<>();
        // Filter orders in a shopping bag by existing user id
        for(Orders o : allOrdersInBag){
            if(o.getUser().getId()==user.getId()){
                ordersList.add(o);
            }
        }

        System.out.println("#[2.2]##############> Existing user shopping bag: "+ordersList.size());

        // Find All order Products by existing user id
        List<Product> shoppingBag = orderServiceImpl.findJoinProductsUserAddToCart(user.getId());
        System.out.println("#[2.2]##############> "+shoppingBag.size());

        List<User> allUsers = userServiceImpl.allUsers();
        for(User u : allUsers){
            System.out.printf("| UserId:    %-20d   |   UserName:    %-35s |\n",u.getId(),u.getFirstName());
            // Find All order Products by Looping through users id (one by one)
            List<Product> shoppingBag1 = orderServiceImpl.findJoinProductsUserAddToCart(u.getId());
            for(Product p : shoppingBag1){
                System.out.printf("| ProductID: %-20d   |   ProductName: %-35s |\n",p.getId(),p.getProductName());
            }
            System.out.println("------------------------------------------------------------------------------------------");
        }



        for (int i = 0; i < shoppingBag.size(); i++) {
            // Covert from int to Long
            Long newId = Long.parseLong(String.valueOf(ordersList.get(i).getId()));
            shoppingBag.get(i).setId(newId);
            System.out.println("#[2.3."+i+"]##############> Order Id: "+shoppingBag.get(i).getId());
        }

        // Total number of products in Shopping Bag
        int totalInCart = shoppingBag.size();

        // Order Summary (in a right side of shoppingCart HTML page)
        double totalPrice=0.0;
        double totalDiscount = 0.0;
        for(Product item : shoppingBag){
            // Total price
            totalPrice+=item.getPrice();
            // Total discount
            totalDiscount+=(item.getDiscount()/100)*item.getPrice();
        }
        // Make total discount negative
        if(totalDiscount>0) totalDiscount*=-1;

        double shipping=0.;
        double tax=0.;
        double freeShipping= 150;  // FREE Standard Shipping on orders $150+
        double taxRate = 6./100.;  // tax rate for example 6%

        if(totalInCart>0){
            if(totalPrice<freeShipping) shipping=10.0;   // Set estimated price for shipping
            tax=totalPrice*taxRate;     // total order tax price
        }

        // Order Summary (in a right side of shoppingCart HTML page)
        double estimatedTotal = totalPrice+totalDiscount+shipping+tax;

        // Total products in a Shopping Bag
        model.addAttribute("shoppingBag", shoppingBag);
        // Shows Total number of products in Shopping Bag
        model.addAttribute("totalInCart",totalInCart);
        // Total price of products in Shopping Bag
        model.addAttribute("totalPrice",totalPrice);
        model.addAttribute("totalDiscount",totalDiscount);
        model.addAttribute("shipping",shipping);
        model.addAttribute("tax",tax);
        model.addAttribute("estimatedTotal",estimatedTotal);

        //return "shopping_bag";
        return "shopping_bag";
    }

    // handler method to handle delete product request
    //
    //@GetMapping("/shoppingCart/{id}")
    //public String deleteItemInCart(@PathVariable Long id)
    @GetMapping("/shoppingCart/deleteItem")
    public String deleteItemInCart(@RequestParam Long id, @RequestParam String index){
        System.out.println("-[3]-----> ShoppingCartController class - deleteItemInCart() method - Endpoint(/shoppingCart/{id}) - EndPoint(redirect:/shopping_cart)");
        System.out.println("-[3.1]---> The List Index is: "+index);
        System.out.println("-[3.2]---> The ID is :"+id);
        orderServiceImpl.deleteOrderFromCart(id);
        return "redirect:/shoppingCart";
    }

}

