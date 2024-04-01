package com.AmericanBoutique.repo;

import com.AmericanBoutique.model.Orders;
import com.AmericanBoutique.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT new com.AmericanBoutique.model.Product(p.id, p.productName, p.size, p.color, p.discount, " +
            "p.description, p.stockQuantity, p.img, p.price) " +
            "FROM Product p " +
            "JOIN Orders o ON p.id = o.product.id " +
            "WHERE o.user.id = :userId")
    List<Product> findProductsByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Orders WHERE id = :oId")
    void deleteOrderFromCart(@Param("oId") Long orderId);

}