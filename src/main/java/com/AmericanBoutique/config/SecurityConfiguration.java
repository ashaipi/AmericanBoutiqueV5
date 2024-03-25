package com.AmericanBoutique.config;

import com.AmericanBoutique.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration  {
// We will create userService class in an upcoming step
   @Autowired
   private UserServiceImpl userService;

   /*@Override
   protected void configure(HttpSecurity http) throws Exception {
       http
               .authorizeRequests()
                   .antMatchers(
                           "/registration**",
                           "/js/**",
                           "/css/**",
                           "/img/**",
                           "/webjars/**").permitAll()
                   .anyRequest().authenticated()
               .and()
                   .formLogin()
                       .loginPage("/login")
                           .permitAll()
               .and()
                   .logout()
                       .invalidateHttpSession(true)
                       .clearAuthentication(true)
                       .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                       .logoutSuccessUrl("/login?logout")
               .permitAll()
               // Handle logout
               .and()
               .logout().invalidateHttpSession(true).clearAuthentication(true)
               .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
               .logoutSuccessUrl("/logoutSuccess").permitAll();
   }
*/

    /**
     * This method using Spring Security for configures security filters for the application, allowing certain URLs to
     * be accessed without authentication, requiring authentication for all other URLs, and specifying login and logout
     * behavior
     */

    @Bean  // This annotation to return an object that will be managed by the Spring container
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       // SecurityFilterChain bean, which is a chain of filters that Spring Security will use to secure HTTP requests.
       // HttpSecurity is a class provided by Spring Security for configuring web-based security for HTTP requests.

       System.out.println("-[1]---> SecurityConfiguration class - filterChain() method - return(http.build())");

       // This to configure the security rules for the application
       // .authorizeHttpRequests() starts configuring authorization rules for HTTP requests.
       // .requestMatchers(...) specifies that requests matching the specified patterns should be permitted without authentication.
       http.authorizeHttpRequests().requestMatchers(
                       "/registration**",
                       "/js/**",
                       "/css/**",
                       "/images/**",
                       "/webjars/**")
               // .anyRequest().authenticated() specifies that any other request should be authenticated.
               .permitAll().anyRequest().authenticated()
               // .and().formLogin() configures form-based login.
               .and().formLogin()
               // .loginPage("/login") specifies the login page URL.
               .loginPage("/login")
               .permitAll().and()
               // .logout() configures logout settings.
               .logout()
               // .invalidateHttpSession(true) invalidates the HTTP session on logout.
               .invalidateHttpSession(true)
               // .clearAuthentication(true) clears the authentication on logout.
               .clearAuthentication(true)
               .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) //  specifies the URL for logout.
               .logoutSuccessUrl("/login?logout") // specifies the URL to redirect to after logout.
               .permitAll(); //  permits all users to access the login and logout pages.

       // The configured HttpSecurity object is built and returned. This completes the configuration of the security
       // filter chain, and the bean is created and managed by the Spring container.
       return http.build();
    }

   //You may declare @Bean methods as static, allowing for them to be called without
   //creating their containing configuration class as an instance
   // this eliminates having to do this in-app prop file:
   // spring.main.allow-circular-references: true
   @Bean
   public static BCryptPasswordEncoder passwordEncoder(){
       System.out.println("-[2]---> SecurityConfiguration class - passwordEncoder() method - return(BCryptPasswordEncoder())");
       return new BCryptPasswordEncoder();
   }

   @Bean
   public DaoAuthenticationProvider authenticationProvider(){
       System.out.println("-[3]---> SecurityConfiguration class - authenticationProvider() method - return(auth())");
       DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
       auth.setUserDetailsService(userService);
       auth.setPasswordEncoder(passwordEncoder());
       return auth;
   }

   protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       System.out.println("-[3]---> SecurityConfiguration class - configure() method - no return");
       auth.authenticationProvider(authenticationProvider());
   }

}