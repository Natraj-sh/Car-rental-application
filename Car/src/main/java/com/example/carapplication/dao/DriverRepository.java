package com.example.carapplication.dao;

import com.example.carapplication.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver,Integer> {
    Optional<Driver> findByName(String name);
}
