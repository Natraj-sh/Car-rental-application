package com.example.carapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Location {
    @Id
    private int locationCode;
    private String locationName;
    @ManyToMany(mappedBy = "locations",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Car> cars;


    @OneToMany(mappedBy = "location",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Driver> driverList;

    @OneToMany(mappedBy = "customerLocation",cascade = CascadeType.ALL)
    @JsonIgnore
    private  List<Customer> customerList;

    public Location(int locationCode, String locationName) {
        this.locationCode = locationCode;
        this.locationName = locationName;
    }
}
