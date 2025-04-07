package com.example.carapplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OTP {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

private int number;
@ManyToOne
private Customer customer;
@ManyToOne
private Driver driver;
}
