package com.example.carapplication.service;

import com.example.carapplication.dto.CarDTO;
import com.example.carapplication.dto.CarRequest;
import com.example.carapplication.exceptions.CarNotFoundException;
import com.example.carapplication.exceptions.DriverIsNotAvailabileException;
import com.example.carapplication.exceptions.DriverNotFoundException;
import com.example.carapplication.exceptions.LocationNotFoundException;

import java.util.List;

public interface CarServiceInterface {
    public CarDTO addCar(CarRequest car) throws DriverNotFoundException, DriverIsNotAvailabileException;

    List<CarDTO> getCars();

    List<CarDTO> getCarsByMake(String make);

    List<CarDTO> getCarsByPrice(int price);

    CarDTO setLocation(int carId, int locationId) throws CarNotFoundException, LocationNotFoundException, DriverIsNotAvailabileException;

    List<CarDTO> getCarsBasedOnLocation(String locationName) throws LocationNotFoundException;
}
