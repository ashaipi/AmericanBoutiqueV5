package com.AmericanBoutique.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private Long orderId; // This field will not be persisted in the database

    @Column(nullable = false)
    private String productName;
    private double price;
    private String size;
    private String color;
    private int stockQuantity;
    private double discount;
    private String img;
    private String barcode;
    private String description;
    private String note;



    // Constructor
    public Product(String productName, String description, double price, int stockQuantity) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public Product(Long id, String productName, String description, int stockQuantity, String img, double price) {
        this.orderId = id;
        this.productName = productName;
        this.description = description;
        this.stockQuantity = stockQuantity;
        this.img = img;
        this.price = price;
    }

}
