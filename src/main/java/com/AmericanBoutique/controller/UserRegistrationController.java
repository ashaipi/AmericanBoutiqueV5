package com.AmericanBoutique.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.AmericanBoutique.model.User;
import com.AmericanBoutique.repo.UserRegistrationDto;
import com.AmericanBoutique.service.UserService;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

   @Autowired
   private UserService userService;

   @ModelAttribute("user")
   public UserRegistrationDto userRegistrationDto() {
	   System.out.println("-[1]---> UserRegistrationController class - UserRegistrationDto() method - Endpoint() - return()");
       return new UserRegistrationDto();
   }

   @GetMapping
   public String showRegistrationForm(Model model) {
	   System.out.println("-[2]---> UserRegistrationController class - showRegistrationForm()");
       return "registration";
   }

   @PostMapping
   public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto, BindingResult result){

	   System.out.println("-[1]---> UserRegistrationController class - registerUserAccount method -   POST MAPPING UserRegController->registerUserAccount()");
       User existing = userService.findByEmail(userDto.getEmail());
       if (existing != null){
           result.rejectValue("email", null, "There is already an account registered with that email");
       }
       System.out.println("-------> result:"+result.toString());
       if (result.hasErrors()){
    	   System.out.println("-------> result hasErrors:"+result.toString());
           return "registration";
       }

       userService.save(userDto);
       return "redirect:/registration?success";
   }
}