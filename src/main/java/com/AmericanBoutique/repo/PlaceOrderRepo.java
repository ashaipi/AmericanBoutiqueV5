package com.AmericanBoutique.repo;

import com.AmericanBoutique.model.PlaceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceOrderRepo extends JpaRepository<PlaceOrder, Long> {

}
