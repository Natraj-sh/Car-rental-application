package com.example.carapplication.controller;

import com.example.carapplication.dao.CustomerRepository;
import com.example.carapplication.dto.CarDTO;
import com.example.carapplication.dto.CustomerCarResponse;
import com.example.carapplication.dto.CustomerDTO;
import com.example.carapplication.dto.CustomerRequest;
import com.example.carapplication.exceptions.*;
import com.example.carapplication.model.Car;
import com.example.carapplication.model.Customer;
import com.example.carapplication.service.CarService;
import com.example.carapplication.service.CutomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
   private CutomerService cutomerService;

    @Autowired
    private CarService carService;

   @PostMapping()
    public CustomerDTO addCustomer(@RequestBody CustomerRequest customerRequest) throws CustomerNotFoundException, DriverIsNotAvailabileException {
       return this.cutomerService.addCustomer(customerRequest);
   }
   @GetMapping("/{customerId}")
    public List<CustomerCarResponse> getCarsBasedOnLocation(@PathVariable int customerId) throws CustomerNotFoundException {
       return this.cutomerService.getCarsBasedOnLocation(customerId);
   }
   @PutMapping("/buy/{customerId}/{entryDate}/{returnDate}")
    public CustomerDTO buyCars(@RequestParam (name = "carName") String name, @PathVariable int customerId, @PathVariable LocalDate entryDate,@PathVariable LocalDate returnDate) throws CarNotFoundException, CustomerNotFoundException, CarNotAvailabileException, DriverIsNotAvailabileException, DriverNotFoundException {
       return this.cutomerService.buyCars(customerId,name,entryDate,returnDate);
   }

   @GetMapping("/getCustomerDetails/{customerId}")
    public CustomerDTO getDetails(@PathVariable int customerId) throws CustomerNotFoundException, DriverIsNotAvailabileException {
       return this.cutomerService.getCustomerDetails(customerId);
   }
    @GetMapping("/getByMake/{make}")
    public List<CustomerCarResponse> getCarsByMake(@PathVariable String make){
        return this.cutomerService.getCarsByMake(make);
    }
    @GetMapping("/getByPrice/{price}")
    public List<CustomerCarResponse> getCarsByPrice(@PathVariable int price){
        return this.cutomerService.getCarsByPrice(price);
    }
    @PostMapping("/giveReview/{customerId}/{driverName}")
    public Customer setReview(@PathVariable int customerId,@PathVariable String driverName,@RequestParam(name = "review") String reviewMessage) throws CustomerNotFoundException, DriverNotFoundException {
       return this.cutomerService.setReview(customerId,driverName,reviewMessage);
    }
    @GetMapping("/getOTP/{customerId}")
    public Long getOTP(@PathVariable int customerId) throws CustomerNotFoundException, OTPnotAvailableException {
       return this.cutomerService.getOTP(customerId);
    }
}
