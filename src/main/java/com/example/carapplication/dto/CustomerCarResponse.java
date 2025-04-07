package com.example.carapplication.dto;

import com.example.carapplication.model.Car;
import lombok.*;

import java.util.List;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCarResponse {
    private String make;
    private String name;

    private String color;

    private int price;

}
