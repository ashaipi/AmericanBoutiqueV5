package com.AmericanBoutique.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class UserInformation {

    @Id // the primary key of the entity
    @GeneratedValue(strategy = GenerationType.AUTO) // Specifies the strategy for generating primary key values
    private Long id;
    private String phone;
    private String address1;
    private String address2;
    private String city;
    private String zipCode;
    private String state;
    //-------------------------
    private String cardholderName;
    private String cardNumber;
    private String expiredDate;
    private String cvv;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public UserInformation(String phone, String address1, String address2, String city, String zipCode,
                           String state, String cardholderName, String cardNumber, String expiredDate,
                           String cvv, User user) {
        this.phone = phone;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.zipCode = zipCode;
        this.state = state;
        this.cardholderName = cardholderName;
        this.cardNumber = cardNumber;
        this.expiredDate = expiredDate;
        this.cvv = cvv;
        this.user = user;
    }
}
