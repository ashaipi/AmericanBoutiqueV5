package com.AmericanBoutique.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Role {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   private String name;

   public Role(String name) {
       this.name = name;
   }

   @Override
   public String toString() {
	   return name;
   }
}
