package com.example.carapplication.controller;

import com.example.carapplication.dao.DriverRepository;
import com.example.carapplication.dto.DriverDTO;
import com.example.carapplication.exceptions.*;
import com.example.carapplication.model.DriverJob;
import com.example.carapplication.dto.DriverRequest;
import com.example.carapplication.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/driver")
public class DriverController {
@Autowired
DriverService driverService;

@Autowired
    DriverRepository driverRepository;

//CarService obj = new CarService();

@PostMapping()
public DriverDTO registerDriver(@RequestBody DriverRequest driver) throws LocationNotFoundException {
    return this.driverService.registerDriver(driver);

}
    @PutMapping("/{driverId}/{carId}")
public DriverDTO updateDriver(@PathVariable int driverId, @PathVariable int carId) throws DriverNotFoundException, CarNotFoundException , MaximumCarDrivableException {
 return this.driverService.updateDriver(driverId,carId);
}
@GetMapping()
    public List<DriverDTO> getAllDriver(){
    return this.driverService.getAllDriver();

}

@GetMapping("/{driverId}")
    public DriverDTO getDriver(@PathVariable int driverId) throws DriverNotFoundException{
    return this.driverService.getDriver(driverId);
}
@PutMapping("/addlocation/{driverId}/{locationId}")
    public DriverDTO setDriverLocation(@PathVariable int driverId,@PathVariable int locationId) throws DriverNotFoundException, LocationNotFoundException {
     return this.driverService.setDriverLocation(driverId,locationId);
}
@GetMapping("/showAllJob/{driverId}")
    public List<DriverJob> showAllJob(@PathVariable int driverId) throws DriverNotFoundException {
    return this.driverService.showAllJobs(driverId);
}
@PutMapping("/assignJob/{driverId}")
    public DriverJob assignJobToDriver(@PathVariable int driverId,@RequestParam (name = "driverJobId") int driverJobId) throws DriverNotFoundException {
     return this.driverService.assignJobToDriver(driverId,driverJobId);
}
@GetMapping("/generateOTP/{driverId}/{customerName}")
    public String GenerateOtpForCustomer(@PathVariable int driverId,@PathVariable String customerName) throws DriverNotFoundException, CustomerNotFoundException {
     return this.driverService.GenerateOtpForCustomer(driverId,customerName);
}
    @GetMapping("/enterOTP/{driverId}/{customerName}/{OTP}")
    public String DeleiverCar(@PathVariable int driverId,@PathVariable String customerName,@PathVariable int OTP) throws DriverNotFoundException, CustomerNotFoundException, OTPMissMatchException {
    return this.driverService.DeleiverCar(driverId,customerName,OTP);
    }
}


