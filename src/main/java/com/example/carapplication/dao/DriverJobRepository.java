package com.example.carapplication.dao;

import com.example.carapplication.model.Driver;
import com.example.carapplication.model.DriverJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverJobRepository extends JpaRepository<DriverJob,Integer> {
  List<DriverJob> findBycustomerName(String customerName);
}
