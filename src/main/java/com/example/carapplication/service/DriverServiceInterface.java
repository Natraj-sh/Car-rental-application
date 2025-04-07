package com.example.carapplication.service;

import com.example.carapplication.dto.DriverDTO;
import com.example.carapplication.exceptions.*;
import com.example.carapplication.model.DriverJob;
import com.example.carapplication.dto.DriverRequest;

import java.util.List;

public interface DriverServiceInterface {
    DriverDTO registerDriver(DriverRequest driver) throws LocationNotFoundException;

    DriverDTO updateDriver(int car, int id) throws DriverNotFoundException, CarNotFoundException, MaximumCarDrivableException;

    List<DriverDTO> getAllDriver();

    DriverDTO getDriver(int driverId) throws DriverNotFoundException;

    DriverDTO setDriverLocation(int driverId, int locationId) throws DriverNotFoundException, LocationNotFoundException;

    List<DriverJob> showAllJobs(int driverId) throws DriverNotFoundException;

    DriverJob assignJobToDriver(int driverId, int driverJobId) throws DriverNotFoundException;

    String GenerateOtpForCustomer(int driverId,String customerName) throws DriverNotFoundException, CustomerNotFoundException;

    String DeleiverCar(int driverId, String customerName, int otp) throws DriverNotFoundException, CustomerNotFoundException, OTPMissMatchException;
}
