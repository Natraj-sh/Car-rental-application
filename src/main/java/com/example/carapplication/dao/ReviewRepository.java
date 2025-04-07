package com.example.carapplication.dao;

import com.example.carapplication.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Integer> {

}
