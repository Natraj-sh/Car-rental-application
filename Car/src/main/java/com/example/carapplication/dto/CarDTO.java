package com.example.carapplication.dto;

import com.example.carapplication.model.Location;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Data
@NoArgsConstructor
public class CarDTO {
    private int id;

    private  String make;

    private String name;

    private List<Location> locations;

    private int driver_id;
    private  String color;
    private int price;
    private int availability;




}
