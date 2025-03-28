package com.example.carapplication.controller;

import com.example.carapplication.dao.CarRepository;
import com.example.carapplication.dto.CarDTO;
import com.example.carapplication.dto.CarRequest;
import com.example.carapplication.exceptions.CarNotFoundException;
import com.example.carapplication.exceptions.DriverIsNotAvailabileException;
import com.example.carapplication.exceptions.DriverNotFoundException;
import com.example.carapplication.exceptions.LocationNotFoundException;
import com.example.carapplication.model.Car;
import com.example.carapplication.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    @Autowired
    CarService carService;

    @Autowired
    CarRepository carRepository;



    @PostMapping()
    public CarDTO addCar(@RequestBody CarRequest car) throws DriverNotFoundException, DriverIsNotAvailabileException {
        return  this.carService.addCar(car);
        //return this.carRepository.save(car);
    }

    @GetMapping()
    public List<CarDTO> getCars(){
        return this.carService.getCars();
    }

//    @GetMapping("/{make}")
//    public List<CarDTO> getCarsByMake(@PathVariable String make){
//        return this.carService.getCarsByMake(make);
//    }
//    @GetMapping("/{price}")
//    public List<CarDTO> getCarsByPrice(@PathVariable int price){
//        return this.carService.getCarsByPrice(price);
//    }

    @PutMapping("/{carId}/{locationId}")
    public CarDTO setLocation(@PathVariable int carId,@PathVariable int locationId) throws CarNotFoundException, LocationNotFoundException, DriverIsNotAvailabileException {
        return this.carService.setLocation(carId,locationId);
    }
    @GetMapping("/location")
    public List<CarDTO> getCarsBasedOnLocation(@RequestParam (name = "location") String locationName ) throws LocationNotFoundException {
        return this.carService.getCarsBasedOnLocation(locationName);
    }





}
