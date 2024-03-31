package com.AmericanBoutique.controller;

import java.util.Collection;

import com.AmericanBoutique.model.User;
import com.AmericanBoutique.service.OrderServiceImpl;
import com.AmericanBoutique.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.AmericanBoutique.service.UserService;

@Controller
public class MainController {

	private final UserService userService;
	private final OrderServiceImpl orderService;
	private final ProductService productService;

	@Autowired
	public MainController(ProductService productService,
						  OrderServiceImpl orderService,
						  UserService userService) {
		super();
		this.productService = productService;
		this.orderService = orderService;
		this.userService = userService;
	}

	//
	// UPDATE users_roles user_id to point to role_id that is superuser in role table
	// to enable that id to be superuser!
	//
	// example:
	// TODO:    mysql> update role set name = 'ROLE_SUPER' where id = 2;
	//

	// Whenever makes a GET request to the root URL, this handler method will be invoked to handle the request.
	@GetMapping("/")
	public String root(HttpSession session, Authentication authentication) {

		System.out.println("-[1]---> MainController class - root() method - Endpoint(/) - HTML(redirect:/products) or HTML(redirect:/home)");
		session.getAttributeNames();
		System.out.println("################################### "+session.getAttributeNames());
		System.out.println(">>>>>>>USER ="+authentication.getName());
    	User existing = userService.findByEmail(authentication.getName());
	   	System.out.println("User firstName="+existing.getFirstName());
	   	System.out.println("User lastName="+existing.getLastName());
    	System.out.println("User Id="+existing.getId());
		System.out.println("USER ROLE="+existing.getRoles());
		
		Collection<? extends GrantedAuthority> userRoles;

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			userRoles = ((UserDetails) principal).getAuthorities();

			for (GrantedAuthority userRole : userRoles) {
				if (userRole.getAuthority().equals("ROLE_SUPER")) {
					System.out.println("USER ROLE="+userRole.getAuthority());
					return "redirect:/products";
				}
			}
		}
		System.out.println("USER ROLE Defaults to Regular USER");
		return "redirect:/home";
	}

	// Home page
	@GetMapping("/home")
	public String home(Model model, Authentication authentication){
		System.out.println("-[2]---> MainController class - home() method - Endpoint(/home) - HTML(home)");
		model.addAttribute("products", productService.getAllProducts());

		// Total orders in a shopping bag
		int totalInCart = orderService.getTotalInBag();
		System.out.println("-[2.1]--> Total in shopping bag: "+totalInCart);

		model.addAttribute("totalInCart",totalInCart);

		User existing = userService.findByEmail(authentication.getName());
		System.out.println("-[2.2]--> Sign in Name: "+existing.getFirstName());
		model.addAttribute("userName",existing.getFirstName());

		return "home";
	}

	// Sign in
    @GetMapping("/login")
    public String login(Model model) {
		System.out.println("-[3]---> MainController class - login() method - Endpoint(/login) - HTML(login)");
        return "login";
    }

    @GetMapping("/user")
    public String userIndex() {
		System.out.println("-[4]---> MainController class - userIndex() method - Endpoint(/user) - HTML(user/index)");
        return "user/index";
    }

	// Sign out
    @ResponseBody
	@GetMapping("/logoutSuccess")
    public String logoutResponse()
    {
		System.out.println("-[5]---> MainController class - logoutResponse() method - Endpoint(/logoutSuccess) - Logged Out!!!!");
        return "Logged Out!!!!";
    }


}
