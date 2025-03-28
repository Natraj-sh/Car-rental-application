package com.example.carapplication.dao;

import com.example.carapplication.model.Customer;
import com.example.carapplication.model.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OTPRepository extends JpaRepository<OTP,Integer> {
    Optional<OTP> findBynumber(int number);
    void deleteByNumber(int number);
    List<OTP> findByCustomer_Id(int id);
}
