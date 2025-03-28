package com.example.carapplication.service;

import com.example.carapplication.dao.*;
import com.example.carapplication.dto.CarDTO;
import com.example.carapplication.dto.DriverDTO;
import com.example.carapplication.exceptions.*;
import com.example.carapplication.model.*;
import com.example.carapplication.dto.DriverRequest;
import com.sun.net.httpserver.Authenticator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DriverService implements DriverServiceInterface{
    @Autowired
    DriverRepository driverRepository;
    @Autowired
    CarRepository carRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    DriverJobRepository driverJobRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    OTPRepository otpRepository;


    @Override
    public DriverDTO registerDriver(DriverRequest driverRequest) throws LocationNotFoundException {

        Driver driver = new Driver();
        List<Car> carList=new ArrayList<>();

        driver = modelMapper.map(driverRequest,Driver.class);
        //driver.setWorkStatus(false);
        Location location = locationRepository.findByLocationName(driverRequest.getLocationName()).orElseThrow(()->new LocationNotFoundException("location not availabile"));
        driver.setLocation(location);
                if(driverRequest.getCarNames()!=null){
             carList =this.carRepository.findByNameIn(driverRequest.getCarNames());
            driver.setCarList(carList);
        }
                //setting each driver to the car which he has
        Driver finalDriver = driver;
           List<Car> carList1 = carList.stream()
                       .map(car -> {
             car.setDriver(finalDriver);
             return car;
         }).toList();
              carList1.stream().map(car -> carRepository.save(car));
              driver.setWorkStatus(false);
        this.driverRepository.save(driver);

        return convertToDto(driver);
        //return modelMapper.map(driver,DriverDTO.class);
    }

    @Override
    public DriverDTO updateDriver (int driverId, int carId) throws DriverNotFoundException, CarNotFoundException,MaximumCarDrivableException {

        //       Optional<Driver> driverOptional  = driverRepository.findById(driverId);
//        Optional<Car> carOptional = carRepository.findById(carId);
////        if(driverOptional.isEmpty())
////            throw new DriverNotFoundException("Driver is not registered");
//        if(carOptional.isPresent()){
//            Driver driver = driverOptional.get();
//            Car car = carOptional.get();
//            List<Car> carList =driver.getCarList();
//            if(carList.isEmpty())
//                 carList = new ArrayList<>();
//            carList.add(car);
//            driver.setCarList(carList);
//            car.setDriver(driver);
//            return driver;
//
//        }
//        throw new CarNotFoundException("This car is not registered ");

        Driver driver = driverRepository.findById(driverId).orElseThrow(() ->new DriverNotFoundException("The driver id is not registered"));
        if(driver.getCarList().size()>2) throw new MaximumCarDrivableException("Only 2 car can be assigned to a driver");
        Car car = carRepository.findById(carId).orElseThrow(() ->new CarNotFoundException("Car is not registered"));

        car.setDriver(driver);
        List<Car> carList =driver.getCarList();
            if(carList.isEmpty()) {
                carList = new ArrayList<>();
            }
            carList.add(car);
            driver.setCarList(carList);
            driverRepository.save(driver);
            carRepository.save(car);

        //return modelMapper.map(driver,DriverDTO.class);
        return convertToDto(driver);
    }

    @Override
    public List<DriverDTO> getAllDriver() {
        List<Driver> driverList = driverRepository.findAll();
        List<DriverDTO> driverDTOList;
         driverDTOList=driverList.stream().
                 map(driver -> convertToDto(driver))
                 .toList();
        return driverDTOList;
//        return driverList.stream()
//                .map(driver ->modelMapper.map(driver,DriverDTO.class))
//                .collect(Collectors.toList());
    }

    @Override
    public DriverDTO getDriver(int driverId) throws DriverNotFoundException{
        Optional<Driver> driver=driverRepository.findById(driverId);
        if(driver.isEmpty())
             throw new DriverNotFoundException("the driver is not registered");
        //return modelMapper.map(driver,DriverDTO.class);
       // DriverDTO driverDTO = convertToDto(driver.get());
        return convertToDto(driver.get());

    }

    @Override
    public DriverDTO setDriverLocation(int driverId, int locationId) throws DriverNotFoundException, LocationNotFoundException {
        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new DriverNotFoundException("Driver not registered"));
        Location location = locationRepository.findById(locationId).orElseThrow(()->new LocationNotFoundException("location not found"));
        driver.setLocation(location);
        driverRepository.save(driver);
        locationRepository.save(location);
        return convertToDto(driver);
    }

    @Override
    public List<DriverJob> showAllJobs(int driverId) throws DriverNotFoundException {
        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new DriverNotFoundException("Driver not registered"));
       // DriverJob driverJob = driverJobRepository.findById(driverId).get();
       return driver.getDriverJobs();
    }

    @Override
    public DriverJob assignJobToDriver(int driverId, int driverJobId) throws DriverNotFoundException {
        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new DriverNotFoundException("Driver not registered"));
        DriverJob driverJob = driverJobRepository.findById(driverJobId).get();
        driver.setWorkStatus(true);
        driverJob.setStatus(DriverJob.JobStatus.IN_PROGRESS);
        this.driverRepository.save(driver);
        driverJobRepository.save(driverJob);
        return driverJob;
      //  return null;
    }

    @Override
    public String GenerateOtpForCustomer(int driverId,String customerName) throws DriverNotFoundException, CustomerNotFoundException {
        Driver driver = this.driverRepository.findById(driverId).orElseThrow(()-> new DriverNotFoundException("Driver is not registered"));
        List<DriverJob> customersList = this.driverJobRepository.findBycustomerName(customerName);
        List<DriverJob> customersList1 = customersList.stream().filter(customer ->{
            if(customer.getStatus()!= DriverJob.JobStatus.COMPLETED)
                return true;
            else
                return false;
        }).toList();
        if(customersList1.isEmpty())
         throw new CustomerNotFoundException("No customer availabile");
        Customer customer = this.customerRepository.findByName(customerName).orElseThrow(()-> new CustomerNotFoundException("No customer available"));
        customer.setCustomerOTP(true);
        customerRepository.save(customer);
        return "Succes";
    }

    @Override
    public String DeleiverCar(int driverId, String customerName, int otp) throws DriverNotFoundException, CustomerNotFoundException, OTPMissMatchException {
        Driver driver = this.driverRepository.findById(driverId).orElseThrow(()-> new DriverNotFoundException("Driver is not registered"));
       // DriverJob driverJob = this.driverJobRepository.findBycustomerName(customerName).orElseThrow(()-> new CustomerNotFoundException("No customer available"));
        List<DriverJob> customersList = this.driverJobRepository.findBycustomerName(customerName);
        List<DriverJob> customersList1 = customersList.stream().filter(customer ->{
            if(customer.getStatus()!= DriverJob.JobStatus.COMPLETED)
                return true;
            else
                return false;
        }).toList();
        if(customersList1.isEmpty())
            throw new CustomerNotFoundException("No customer availabile");
        DriverJob driverJob = customersList1.get(0);
        Customer customer = this.customerRepository.findByName(customerName).orElseThrow(()-> new CustomerNotFoundException("No customer available"));
       OTP otpObj =   this.otpRepository.findBynumber(otp).orElseThrow(()-> new OTPMissMatchException("otp miss match"));
        this.otpRepository.deleteById(otpObj.getId());
        driver.setWorkStatus(false);
        driverJob.setStatus(DriverJob.JobStatus.COMPLETED);
        driverJobRepository.save(driverJob);
        driverRepository.save(driver);
          customer.setCustomerOTP(false);
          this.customerRepository.save(customer);
        return "succesfully deleivered";
    }


    public  DriverDTO convertToDto(Driver driver){
//        DriverDTO driverDTO = modelMapper.map(driver,DriverDTO.class);
//        List<CarDTO> carDTOList = driver.getCarList().stream().map(car -> {
//            CarDTO carDTO=modelMapper.map(car,CarDTO.class);
//            carDTO.setDriver_id(car.getDriver().getId());
//            return carDTO;
//        }).toList();
//        driverDTO.setCarList(carDTOList);
//        return driverDTO

        DriverDTO driverDTO = modelMapper.map(driver,DriverDTO.class);
        if(driver.getCarList()==null)
            driverDTO.setCarList(new ArrayList<>());
        else {
            List<CarDTO> carDTOList = driver.getCarList().stream()
                    .map(car -> {
                        CarDTO carDTO = modelMapper.map(car, CarDTO.class);
                        carDTO.setDriver_id(driver.getId());
                        return carDTO;
                    })
                    .toList();
            driverDTO.setCarList(carDTOList);
        }
        return driverDTO;
    }

}
