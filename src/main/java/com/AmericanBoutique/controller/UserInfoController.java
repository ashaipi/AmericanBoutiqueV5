package com.AmericanBoutique.controller;

import com.AmericanBoutique.model.Product;
import com.AmericanBoutique.model.User;
import com.AmericanBoutique.model.UserInformation;
import com.AmericanBoutique.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class UserInfoController {

    private UserInformationServiceImpl userInformationServiceImpl;
    private UserServiceImpl userServiceImpl;
    private OrderServiceImpl orderServiceImpl;

    @Autowired
    public UserInfoController(UserInformationServiceImpl userInformationServiceImpl,
                              OrderServiceImpl orderServiceImpl,
                              UserServiceImpl userServiceImpl) {
        this.userInformationServiceImpl = userInformationServiceImpl;
        this.orderServiceImpl = orderServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    // Add new product
    @GetMapping("/placeOrder")
    public String createUserInfo(Model model, Authentication authentication) {
        System.out.println("-[2]---> UserInfoController class - createProductForm() method - Endpoint(/products/new) - HTML(create_product)");
        // create a product object to hold product form data
        UserInformation userInfo =new UserInformation();
        model.addAttribute("userInfo", userInfo);
        //---------------------------------------------------------
        User existing = userServiceImpl.findByEmail(authentication.getName());
        model.addAttribute("firstName",existing.getFirstName());
        model.addAttribute("lastName",existing.getLastName());
        model.addAttribute("email",existing.getEmail());

        // find user id
        User user = userServiceImpl.findByEmail(authentication.getName());
        // filter order in shopping bag by user id
        List<Product> orderProducts = orderServiceImpl.findJoinProductsUserAddToCart(user.getId());

        int totalInCart = orderProducts.size();
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

        return "place_order";
    }

    // Save product to database
    @PostMapping("/placeOrder")
    public String saveUserInfo(@ModelAttribute("userInfo") UserInformation userInfo, Authentication authentication) {
        System.out.println("-[3]---> UserInfoController class - saveProduct() method - Endpoint(/products) - EndPoint(redirect:/products)");

        User existing = userServiceImpl.findByEmail(authentication.getName());

        userInformationServiceImpl.saveUserInfo(userInfo,authentication);
        return "redirect:/orderShipped";
    }

    @GetMapping("/orderShipped")
    public String orderShipped(Model model, Authentication authentication) throws ParseException {

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
    @GetMapping("/placeOrder")
    public String createUserInfo(Model model, Authentication authentication) {

        UserInformation userInfo = new UserInformation();
        model.addAttribute("userInfo", userInfo);

        User existing = userService.findByEmail(authentication.getName());
        model.addAttribute("firstName",existing.getFirstName());
        model.addAttribute("lastName",existing.getLastName());
        model.addAttribute("email",existing.getEmail());

        //System.out.println("---------> "+userInfo.getAddress1());
/*
        User existingUser = userService.findByEmail(existing.getEmail());
        existingUser.setPhone("-----------111----------> Phone number : "+user.getPhone());
        System.out.println("----- [user.getPhone()] ---> "+user.getPhone());
        existingUser.setAddress1(user.getAddress1());
        System.out.println("----- [user.getAddress1()] ---> "+user.getAddress1());
        existingUser.setAddress2(user.getAddress2());
        existingUser.setCity(user.getCity());
        existingUser.setZipCode(user.getZipCode());
        existingUser.setState(user.getState());

        userService.userUpdate(existingUser);
*/
    /*
        //int totalInCart=100;
        int totalInCart = shoppingCartService.getTotalInCart();
        //System.out.println(totalInCart);
        model.addAttribute("totalInCart",totalInCart);

        double totalPrice = shoppingCartService.getTotalPriceInCart();
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

        return "place_order";
    }

    @PostMapping("/placeOrder")
    public String addUserInfo(@ModelAttribute("userInfo") UserInformation userInfo){
        System.out.println("-[1]---> UserInfoController class - addUserInfo() method - Endpoint(/placeOrder) - HTML(redirect:/home)");

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
