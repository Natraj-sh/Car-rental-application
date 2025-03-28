package com.example.carapplication.dao;

import com.example.carapplication.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
   Optional<Customer> findByName(String name);
}
