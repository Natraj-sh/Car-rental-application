package com.example.carapplication.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Driver {

    @Id
    private int id;

    @Column
    private String name;

    @OneToMany(mappedBy = "driver",cascade = CascadeType.ALL)
    private List<Car> carList;

    @ManyToOne
    private Location location;

    @OneToMany(mappedBy = "driver",cascade = CascadeType.ALL)
    private List<DriverJob> driverJobs=new ArrayList<>();

   private boolean workStatus;
   @OneToMany(mappedBy = "driver",cascade = CascadeType.ALL)
   private List<Review> driverReviews = new ArrayList<>();
   @OneToMany(mappedBy = "driver",cascade = CascadeType.ALL)
   private List<OTP> otpList;
//
//    @OneToOne
//    private DriverJob driverJob;

}
