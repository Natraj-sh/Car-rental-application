package com.example.carapplication.dto;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {
    private String name;
    private String phone;
    private String locationName;
}
