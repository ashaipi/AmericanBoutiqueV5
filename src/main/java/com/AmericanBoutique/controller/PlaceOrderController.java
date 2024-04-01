package com.AmericanBoutique.controller;

import com.AmericanBoutique.model.*;
import com.AmericanBoutique.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class PlaceOrderController {

    private final StateService stateService;
    private final PlaceOrderServiceImpl userInformationServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private final OrderServiceImpl orderServiceImpl;

    @Autowired
    public PlaceOrderController(PlaceOrderServiceImpl userInformationServiceImpl,
                                OrderServiceImpl orderServiceImpl,
                                UserServiceImpl userServiceImpl,
                                StateService stateService) {
        this.userInformationServiceImpl = userInformationServiceImpl;
        this.orderServiceImpl = orderServiceImpl;
        this.userServiceImpl = userServiceImpl;
        this.stateService = stateService;
    }

    // PlaceOrder - Add new user shipping and payment information
    @GetMapping("/placeOrder")
    public String createUserInfo(Model model, Authentication authentication) {
        System.out.println("-[1]---> PlaceOrderController class - createUserInfo() method - Endpoint(/placeOrder) - HTML(place_order)");

        // to find the total in shopping bag
        List<Orders> ordersList = orderServiceImpl.getAllOrders();
        System.out.println("#[1.1]##############> Size of orderList: "+ordersList.size());

        // find user id
        User user = userServiceImpl.findByEmail(authentication.getName());
        System.out.println("------------> User Name: "+user.getFirstName());

        // filter order in shopping bag by user id
        List<Product> shoppingBag = orderServiceImpl.findJoinProductsUserAddToCart(user.getId());
        System.out.println("#[1.2]##############> Total in Shopping Bag: "+shoppingBag.size());

        for (int i = 0; i < shoppingBag.size(); i++) {
            // Covert from int to Long
            System.out.println("#[1.3."+i+"]##############> Product ID: "+shoppingBag.get(i).getId());
            Long newId = Long.parseLong(String.valueOf(ordersList.get(i).getId()));
            shoppingBag.get(i).setId(newId);
            System.out.println("#[1.3."+i+"]##############> Order ID:   "+shoppingBag.get(i).getId());
        }

        // Total number of products in Shopping Bag
        int totalInCart = shoppingBag.size();

        // Order Summary (in a right side of shoppingCart HTML page)
        double totalPrice=0.0;
        double totalDiscount = 0.0;
        for(Product item : shoppingBag){
            totalPrice+=item.getPrice();
            totalDiscount+=(item.getDiscount()/100)*item.getPrice();
        }
        if(totalDiscount>0) totalDiscount*=-1;

        double shipping=0.;
        double tax=0.;
        double freeShipping= 150;  // FREE Standard Shipping on orders $150+
        double taxRate = 6./100.;  // tax rate for example 6%

        if(totalInCart>0){
            if(totalPrice<freeShipping) shipping=10.0;
            tax=totalPrice*taxRate;
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

        // Login user first name, last name, and user login email
        User existing = userServiceImpl.findByEmail(authentication.getName());
        model.addAttribute("firstName",existing.getFirstName());
        model.addAttribute("lastName",existing.getLastName());
        model.addAttribute("email",existing.getEmail());

        // create a PlaceOrder object to hold User Shipping and Payment information form data
        PlaceOrder userInfo =new PlaceOrder();
        model.addAttribute("userInfo", userInfo);
        //---------------------------------------------------------

        List<State> states = stateService.getAllStates();
        model.addAttribute("states", states);

        return "place_order";
    }

    // Save user shipping and payment information to a database
    @PostMapping("/placeOrder")
    public String saveUserInfo(@ModelAttribute("userInfo") PlaceOrder userInfo,
                               Authentication authentication) {
        System.out.println("-[2]---> PlaceOrderController class - saveUserInfo() method - Endpoint(/placeOrder) - EndPoint(orderShipped)");

        User existing = userServiceImpl.findByEmail(authentication.getName());
        userInfo.setUser(existing);

        userInformationServiceImpl.saveUserInfo(userInfo);
        return "redirect:/orderShipped";
    }

    // Conformation: conform shipping and payment
    @GetMapping("/orderShipped")
    public String orderShipped(Model model, Authentication authentication) {

        int  shipAfterDays = 10;  // Days add to existing date for Expected Shipping Day

        User existing = userServiceImpl.findByEmail(authentication.getName());
        model.addAttribute("firstName",existing.getFirstName());

        model.addAttribute("address","39 Spring Ln, Sharon, MA 02067 ");

        // create an instance of the SimpleDateFormat that matches the given date (Today date)
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String todayDate = simpleDateFormat.format(new Date());

        //create an instance of the Calendar class and set the date to the given date
        Calendar cal = Calendar.getInstance();
        try{
            cal.setTime(simpleDateFormat.parse(todayDate));
        }catch(ParseException e){
            e.printStackTrace();
        }

        // use add() method to add the days to the given date
        cal.add(Calendar.DAY_OF_MONTH, shipAfterDays);

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        formatter = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        String expectedShippingDay = formatter.format(cal.getTime());
        System.out.println("Expecting Ship Date: "+expectedShippingDay);

        model.addAttribute("shippingDay", expectedShippingDay);
        return "order_shipped";
    }

    /*

    @PostMapping("/placeOrder")
    public String addUserInfo(@ModelAttribute("userInfo") PlaceOrder userInfo){
        System.out.println("-[1]---> PlaceOrderController class - addUserInfo() method - Endpoint(/placeOrder) - HTML(redirect:/home)");

        userInformationServiceImpl.saveUserInfo(userInfo);

        // Redirect the user to a confirmation page or back to the Place order page
        return "redirect:/placeOrder";
    }

    // conformation
    @GetMapping("/orderShipped")
    public String orderShipped(Model model, Authentication authentication) throws ParseException {

        int  shipAfterDays = 10;  // Days add to existing date for Expected Shipping Day

        User existing = userService.findByEmail(authentication.getName());
        model.addAttribute("firstName",existing.getFirstName());

        model.addAttribute("address","39 Spring Ln, Sharon, MA 02067 ");

        // create an instance of the SimpleDateFormat that matches the given date (Today date)
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String todayDate = simpleDateFormat.format(new Date());

        //create an instance of the Calendar class and set the date to the given date
        Calendar cal = Calendar.getInstance();
        try{
            cal.setTime(simpleDateFormat.parse(todayDate));
        }catch(ParseException e){
            e.printStackTrace();
        }

        // use add() method to add the days to the given date
        cal.add(Calendar.DAY_OF_MONTH, shipAfterDays);

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        formatter = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        String expectedShippingDay = formatter.format(cal.getTime());
        System.out.println("Expecting Ship Date: "+expectedShippingDay);

        model.addAttribute("shippingDay", expectedShippingDay);
        return "order_shipped";
    }

*/
}
