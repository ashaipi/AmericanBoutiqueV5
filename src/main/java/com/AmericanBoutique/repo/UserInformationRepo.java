package com.AmericanBoutique.repo;

import com.AmericanBoutique.model.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInformationRepo extends JpaRepository<UserInformation, Long> {

}
