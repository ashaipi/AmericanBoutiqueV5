package com.AmericanBoutique.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.apache.bcel.classfile.Code;

import java.util.Collection;

@Getter // a Lombok annotation to generate automatically getter methods for all fields.
@Setter // a Lombok annotation to generate automatically setter methods for all fields.
@NoArgsConstructor // a Lombok annotation to generate automatically None Args Constructor.
@Entity // Indicates that this class is a JPA entity and should be mapped to a database table
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
// Specifies details about the table. here, a unique constraint on the "email" column to ensure email uniqueness.
public class User {

   @Id // the primary key of the entity
   @GeneratedValue(strategy = GenerationType.AUTO) // Specifies the strategy for generating primary key values
   private Long id;

   private String firstName;
   private String lastName;
   private String email;
   private String password;

   // Defines a many-to-many relationship between this entity (User) and another entity (Role).
   @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
   // Specifies the details of the join table for the many-to-many relationship.
   @JoinTable(
           name                 = "users_roles",
           joinColumns          = @JoinColumn(name = "user_id", referencedColumnName = "id"),
           inverseJoinColumns   = @JoinColumn(name = "role_id", referencedColumnName = "id"))
   private Collection<Role> roles;

   public User(String firstName, String lastName, String email, String password) {
       this.firstName = firstName;
       this.lastName = lastName;
       this.email = email;
       this.password = password;
   }

   public User(String firstName, String lastName, String email, String password,
               Collection<Role> roles) {
       this.firstName = firstName;
       this.lastName = lastName;
       this.email = email;
       this.password = password;
       this.roles = roles;
   }

   public Collection<Role> getRoles() {
       return roles;
   }

   public void setRoles(Collection<Role> roles) {
       this.roles = roles;
   }

   @Override
   public String toString() {
       return "User{" +
               "id=" + id +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", email='" + email + '\'' +
               ", password='" + "*********" + '\'' +
               ", roles=" + roles +
               '}';
   }
}