package com.AmericanBoutique.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private Long orderId; // This field will not be persisted in the database


    @Column(nullable = false)
    private String productName;
    private String description;
    private double price;
    private int stockQuantity;
    private String img;


    public Product(String productName, String description, double price, int stockQuantity) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // Constructor
    public Product(Long id, String productName, String description, int stockQuantity, String img, double price) {
        this.orderId = id;
        this.productName = productName;
        this.description = description;
        this.stockQuantity = stockQuantity;
        this.img = img;
        this.price = price;
    }

}
