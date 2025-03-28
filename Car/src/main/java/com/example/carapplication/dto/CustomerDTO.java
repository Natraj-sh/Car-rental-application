package com.example.carapplication.dto;

import com.example.carapplication.model.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CustomerDTO {
    private String name;
    private String phone;
    private Location location;
    private List<CarDTO
            > carDTOList;
}
