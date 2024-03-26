package com.AmericanBoutique.controller;

import com.AmericanBoutique.model.Orders;
import com.AmericanBoutique.model.Product;
import com.AmericanBoutique.model.User;
import com.AmericanBoutique.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class ShoppingCartController {

    private UserServiceImpl userServiceImpl;
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
    //@PostMapping("/addToCart")
    @RequestMapping(value = "/addToCart", method = {RequestMethod.GET, RequestMethod.POST})
    @Transactional
    public String addToCart(@RequestParam(name = "id") Long productId, Authentication authentication) {
        System.out.println("-[1]---> ShoppingCartController class - addToCart() method - Endpoint(/addToCart) - HTML(redirect:/home)");

        Product p = productService.getProductById(productId);

        System.out.println("------------> Product ID is : "+p.getId());
        System.out.println("------------> Product Name is : "+p.getProductName());
        System.out.println("------------> Product Description is : "+p.getDescription());
        System.out.println("------------> Product Price is : "+p.getPrice());

        // Call the CartService to save the product details to the cart table
        // shoppingCartService.addToCart(p.getProductName(), p.getDescription(), p.getPrice(), p.getImg());

        // find user id
        User user = userServiceImpl.findByEmail(authentication.getName());
        System.out.println("------------> User Name: "+user.getFirstName());
        Orders orders = new Orders();
        orders.setUser(user);
        orders.setProduct(p);
        orders.setStatus("Completed");
        try {
            orderServiceImpl.saveOrder(orders);
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            e.printStackTrace();
        }

        // Redirect the user to a confirmation page or back to the product page
        return "redirect:/home";

        // Return a JavaScript alert message
        //return "<script>alert('Item added to cart successfully');</script>";

        // Return a success message
        // return "Item added to cart successfully";
    }

    //
    @GetMapping("/shoppingCart")
    public String listItems(Model model, Authentication authentication) {
        System.out.println("-[2]---> ShoppingCartController class - listItems() method - Endpoint(/shoppingCart) - HTML(shopping_cart)");

        List<Orders> ordersList = orderServiceImpl.getAllOrders();
        System.out.println("#[1]##############> Size of orderList: "+ordersList.size());
        System.out.println("#[3]##############> "+ordersList.get(0).getStatus());
        for (int i = 0; i < ordersList.size(); i++) {
            System.out.println("#[4]##############> orderList ID: "+ordersList.get(i).getId());
        }

        //model.addAttribute("shoppingCart", shoppingCartService.getAllItemsInCart());

        // find user id
        User user = userServiceImpl.findByEmail(authentication.getName());
        System.out.println("------------> User Name: "+user.getFirstName());

        // filter order in shopping bag by user id
        List<Product> orderProducts = orderServiceImpl.findJoinProductsUserAddToCart(user.getId());
        System.out.println("#[1]##############> "+orderProducts.size());
        System.out.println("#[3]##############> "+orderProducts.get(0).getProductName());

        for (int i = 0; i < orderProducts.size(); i++) {
            // Covert from int to Long
            Long newId = Long.parseLong(String.valueOf(ordersList.get(i).getId()));
            orderProducts.get(i).setId(newId);
            System.out.println("#[4]##############> "+orderProducts.get(i).getId());
        }

        for (int i = 0; i < orderProducts.size(); i++) {
            System.out.println(ordersList.get(i).getId()+" <----------> "+ ordersList.get(i).getProduct().getProductName());
            System.out.println(orderProducts.get(i).getId()+" <----------> "+ orderProducts.get(i).getProductName());
        }

        // sen the bag object to Shopping bag page
        model.addAttribute("orderProducts", orderProducts);

        System.out.println("[1]-->"+orderServiceImpl.findJoinProductsUserAddToCart(user.getId()));
        System.out.println("[2]-->"+orderServiceImpl.findJoinProductsUserAddToCart(user.getId()).size());
        System.out.println("[3]-->"+orderServiceImpl.findJoinProductsUserAddToCart(user.getId()).get(0));
        System.out.println("[4]-->"+orderServiceImpl.findJoinProductsUserAddToCart(user.getId()).get(0).getProductName());
        System.out.println("[5]-->"+orderServiceImpl.findJoinProductsUserAddToCart(user.getId()).get(0).getId());

        int totalInCart = orderProducts.size();
        System.out.println("----------------------> Total in Cart: "+totalInCart);
        model.addAttribute("totalInCart",totalInCart);

        double totalPrice=0.0;
        for(Product item : orderProducts){
            totalPrice+=item.getPrice();
        }
        model.addAttribute("totalPrice",totalPrice);

        double discount=0.;
        double shipping=0.;
        double tax=0.;
        double freeShipping= 150. - 0.001;
        double taxRate = 6./100.;  // tax rate for example 6%

        if(totalInCart>0){
            discount=-15.50;
            if(totalPrice<=freeShipping) shipping=10.0;
            tax=totalPrice*taxRate;
        }

        double estimatedTotal = totalPrice+discount+shipping+tax;
        model.addAttribute("discount",discount);
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
        System.out.println("-[6]---> ShoppingCartController class - deleteItemInCart() method - Endpoint(/shoppingCart/{id}) - EndPoint(redirect:/shopping_cart)");
        System.out.println("-[7]---> The List Index is: "+index);
        System.out.println("-[8]---> The ID is :"+id);
        orderServiceImpl.deleteOrderFromCart(id);
        return "redirect:/shoppingCart";
    }

}

