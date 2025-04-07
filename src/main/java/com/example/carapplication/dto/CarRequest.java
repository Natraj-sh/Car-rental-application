package com.example.carapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarRequest {
    private String make;
    private String name;
    private String color;
    private List<String> location;
    private int price;
    private int availability;
}
