package com.example.carapplication.service;

import com.example.carapplication.dto.CustomerCarResponse;
import com.example.carapplication.dto.CustomerDTO;
import com.example.carapplication.dto.CustomerRequest;
import com.example.carapplication.exceptions.*;
import com.example.carapplication.model.Customer;

import java.time.LocalDate;
import java.util.List;

public interface CustomerServiceInterface {
    CustomerDTO addCustomer(CustomerRequest customerRequest) throws CustomerNotFoundException, DriverIsNotAvailabileException;

    List<CustomerCarResponse> getCarsBasedOnLocation(int cutomerId) throws CustomerNotFoundException;

    CustomerDTO buyCars(int customerId, String name, LocalDate date, LocalDate returnDate) throws CarNotFoundException, CustomerNotFoundException, CarNotAvailabileException, DriverIsNotAvailabileException, DriverNotFoundException;

    CustomerDTO getCustomerDetails(int customerId) throws CustomerNotFoundException, DriverIsNotAvailabileException;
    List<CustomerCarResponse> getCarsByPrice(int price);

    List<CustomerCarResponse> getCarsByMake(String make);

    Customer setReview(int cutomerId,String driverName,String reviewMessage) throws CustomerNotFoundException, DriverNotFoundException;

    Long getOTP(int customerId) throws CustomerNotFoundException, OTPnotAvailableException;
}
