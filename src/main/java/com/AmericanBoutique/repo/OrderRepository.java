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
    // Add custom query methods if needed

//    Orders findOrdersByUser(Long user_id);

//    @Query("SELECT p.product_name, p.description, p.stock_quantity, p.img, p.price\n" +
//            "FROM product p\n" +
//            "JOIN orders o ON p.id = o.product_id\n" +
//            "WHERE o.user_id = 1;")
//    public List<String> getJoinProductUserAddInCart();


//    @Query("DELETE FROM com.AmericanBoutique.model.Orders WHERE id = :i_d")
//    void deleteOrderFromCart(Long i_d);

    @Query("SELECT new com.AmericanBoutique.model.Product(p.id, p.productName, p.description, p.stockQuantity, p.img, p.price)\n" +
            "FROM Product p\n" +
            "JOIN Orders o ON p.id = o.product.id\n" +
            "WHERE o.user.id = :userId")
    List<Product> findProductsByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Orders WHERE id = :oId")
    void deleteOrderFromCart(@Param("oId") Long orderId);

}