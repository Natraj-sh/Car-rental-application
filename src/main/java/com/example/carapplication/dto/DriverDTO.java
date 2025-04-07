package com.example.carapplication.dto;

import com.example.carapplication.model.Location;
import com.example.carapplication.model.Review;
import lombok.*;

import java.util.List;
@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverDTO {
    private int id;
    private String name;

    List<CarDTO> carList;

    private Location location;

    private List<Review> driverReviews;

    //    public List<Car> getCarList() {
//        return carList;
//    }
//
//    public void setCarList(List<Car> carList) {
//        this.carList = carList;
//    }
}
