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

    // this constructor used in OrderRepository interface, in a MySQL Query at findProductsByUserId abstract method
    public Product(Long id, String productName, String size, String color, double discount,
                   String description, int stockQuantity, String img, double price) {
        this.id = id;
        this.productName = productName;
        this.size = size;
        this.color = color;
        this.discount = discount;
        this.description = description;
        this.stockQuantity = stockQuantity;
        this.img = img;
        this.price = price;
    }

}
