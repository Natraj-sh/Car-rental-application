package com.example.carapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverRequest {
    private int id;
    private String name;
    private String LocationName;
    private List<String> carNames;
}
