package com.AmericanBoutique.repo;

import com.AmericanBoutique.model.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color, Long> {
}
