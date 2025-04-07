package com.example.carapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String name;
    @Column
    private String phone;
    @ManyToOne
    private Location customerLocation;
    @ManyToMany
    @JsonIgnore
    private List<Car> customerCarList;
    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<Review> customerReviews = new ArrayList<>();

    private boolean customerOTP;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OTP> otpList;
}
