package com.example.carapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private  String make;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    //@JsonIgnore
    private Driver driver;

    @Column
    private  String color;

    @Column
    private int price;
    @ManyToMany
    private List<Location> locations;

    @ManyToMany(mappedBy = "customerCarList")
    private List<Customer> customersList;
//
    @Column
    private int availability;


}
