package com.AmericanBoutique.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AmericanBoutique.model.User;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByEmail(String email);
}


