package com.example.carapplication.service;

import com.example.carapplication.dao.*;
import com.example.carapplication.dto.*;
import com.example.carapplication.exceptions.*;
import com.example.carapplication.model.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class CutomerService implements CustomerServiceInterface{
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    CarRepository carRepository;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    DriverJobRepository driverJobRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    OTPRepository otpRepository;

    @Override
    public CustomerDTO addCustomer(CustomerRequest customerRequest) throws CustomerNotFoundException, DriverIsNotAvailabileException {
        Customer customer = new Customer();

         customer = modelMapper.map(customerRequest,Customer.class);
//        customer.setId(UUID.randomUUID().toString());
         Location location= locationRepository.findByLocationName(customerRequest.getLocationName()).orElseThrow(() -> new CustomerNotFoundException("not found"));
        customer.setCustomerLocation(location);
        customer.setCustomerOTP(false);
        customerRepository.save(customer);
        return convertToCustomerDto(customer);
    }

    @Override
    public List<CustomerCarResponse> getCarsBasedOnLocation(int cutomerId) throws CustomerNotFoundException {
        Customer customer = findCustomer(cutomerId);


      int customerLocationCode=  customer.getCustomerLocation().getLocationCode();

      List<Car> carList = carRepository.findAll().stream()
               .filter(car -> {
                   List<Integer> locationsCode = car.getLocations().stream().map(location -> location.getLocationCode()).toList();
                   if(locationsCode.contains(customerLocationCode))
                       return true;
                   else
                       return false;
               }).toList();
        List<String> carsMakeName = new ArrayList<>();
        List<List<Car>> carListsOfMake = new ArrayList<>();
        List<Car> flattenedList = new ArrayList<>();
        if(customer.getCustomerCarList().size()!=0){
            carsMakeName = customer.getCustomerCarList().stream()
                   .map(car -> car.getMake()).toList();
        carListsOfMake = carsMakeName.stream().map(make -> this.carRepository.findByMake(make)).toList();
         //as each make can have multiple cars eg ford has mustang and endevour so we need a list <List<Car>>
           //IP:{ford,supra}
            //to save oP:{mustang,endevour},{supra}
            //to conver list of list to single list use flat map
          flattenedList = carListsOfMake.stream()
                 .flatMap(List::stream).toList();
        }
        if(flattenedList.size()!=0) {
            //  "java.lang.UnsupportedOperationException
//            for (Car car : flattenedList) {
//                if(!carList.contains(car))
//             carList.add(car);
//            }
            return flattenedList.stream()
                    .map(car -> covertToCarDto(car)).toList();
        }

      //also can do like this
        List<Car> carList1 = carRepository.findAll().stream().filter(car -> {
           Boolean check = car.getLocations().stream()
                    .anyMatch(location -> location.getLocationCode()==customerLocationCode);
           if(check)
               return true;
           return false;
        }).toList();
        List<Car> carList2 = this.carRepository.findAll().stream()
                .filter(car -> car.getLocations().stream()
                        .anyMatch(location -> location.getLocationCode()==customerLocationCode)).toList();
        return carList.stream()
               .map(car -> covertToCarDto(car)).toList();


    }

    @Override
    public CustomerDTO buyCars(int customerId, String name, LocalDate entryDate, LocalDate returnDate) throws CarNotFoundException, CustomerNotFoundException, CarNotAvailabileException, DriverIsNotAvailabileException, DriverNotFoundException {
        Car car = this.carRepository.findByName(name)
                .orElseThrow(() -> new CarNotFoundException("car not found"));
        Customer customer = this.customerRepository.findById(customerId).orElseThrow(() ->new CustomerNotFoundException("customer not found"));
        Driver driver = car.getDriver();
        List<Car> carList =customer.getCustomerCarList();
        List<Customer> customerList = car.getCustomersList();
        if(driver==null)
            throw  new DriverNotFoundException("driver is not availbile to deleiver");
        if (carList.isEmpty())
            carList = new ArrayList<>();
        if (customerList.isEmpty())
            customerList = new ArrayList<>();
        if (car.getAvailability() > 0) {
            int total = car.getAvailability()-1;
            car.setAvailability(total);
            carList.add(car);
            customerList.add(customer);
            customer.setCustomerCarList(carList);
            car.setCustomersList(customerList);
            if(driver.isWorkStatus())
                throw new DriverIsNotAvailabileException("driver is not availbile to deleiver");
            //DriverJobResponse driverJobResponse = new DriverJobResponse();
//            driver.setWorkStatus(true);
            DriverJob driverJob = DriverJob.builder()
                    .carName(car.getName())
                    .customerName(customer.getName())
                    .customerLocation(customer.getCustomerLocation().getLocationName())
                    .carLocations(car.getLocations().stream().map(location -> location.getLocationName()).toList())
                    .driver(driver)
                    .entryDate(entryDate)
                    .returnDate(returnDate)
                    .status(DriverJob.JobStatus.NOT_STARTED)
                       .build();
//            driver.setDriverJob(driverJob);
//            driverRepository.save(driver);
            List<DriverJob> driverJobs = driver.getDriverJobs();
            driverJobs.add(driverJob);
            driver.setDriverJobs(driverJobs);
            driverJobRepository.save(driverJob);
            customerRepository.save(customer);
            carRepository.save(car);
            return convertToCustomerDto(customer);

        }
        else
           throw new CarNotAvailabileException("car availability is over");
    }

    @Override
    public CustomerDTO getCustomerDetails(int customerId) throws CustomerNotFoundException, DriverIsNotAvailabileException {
        Customer customer= this.customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("not found"));
        return convertToCustomerDto(customer);

    }

    @Override
    public List<CustomerCarResponse> getCarsByPrice(int price) {
        List<Car> carList = this.carRepository.findAll().stream()
                .filter(car -> car.getPrice()<price).toList();
        return carList.stream().map(car -> covertToCarDto(car)).toList();
    }

    @Override
    public List<CustomerCarResponse> getCarsByMake(String make) {
        List<Car> carList = this.carRepository.findByMake(make);
        return carList.stream().map(car -> covertToCarDto(car)).toList();
    }

    @Override
    public Customer setReview(int customerId, String driverName, String reviewMessage) throws CustomerNotFoundException, DriverNotFoundException {
        Customer customer= this.customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("not found"));
        Driver driver = this.driverRepository.findByName(driverName).orElseThrow(() -> new DriverNotFoundException("driver with name not availabile"));
        Review review = Review.builder()
                .message(reviewMessage)
                .customer(customer)
                .driver(driver)
                .build();
        List<Review> customerReviews = customer.getCustomerReviews();
        customerReviews.add(review);
        List<Review> driverReviews = driver.getDriverReviews();
        driverReviews.add(review);
        customer.setCustomerReviews(customerReviews);
        driver.setDriverReviews(driverReviews);
        reviewRepository.save(review);
        customerRepository.save(customer);
        driverRepository.save(driver);
        return customer;

    }

    @Override
    public Long getOTP(int customerId) throws CustomerNotFoundException, OTPnotAvailableException {
        Random rand = new Random();
        Customer customer= this.customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("not found"));
        List<OTP> ifCustomerPresentList = this.otpRepository.findByCustomer_Id(customerId);
        List<Car> carList = customer.getCustomerCarList();
        Car car = carList.get(carList.size()-1);
        if(ifCustomerPresentList.isEmpty())
        {
            if(customer.isCustomerOTP())
            {

                // Generate a random number between 100000 and 999999 (inclusive)
                Long otpNumber= rand.nextInt(900000) + 100000L;
                int otpnum = otpNumber.intValue();
                OTP otp = OTP.builder()
                        .number(otpnum)
                        .customer(customer)
                        .driver(car.getDriver())
                        .build();
                otpRepository.save(otp);
                return otpNumber;
            }
        }
        else{
            OTP  otpObj= ifCustomerPresentList.get(0);
            if(customer.isCustomerOTP())
            {
               this.otpRepository.deleteById(otpObj.getId());
                // Generate a random number between 100000 and 999999 (inclusive)
                Long otpNumber= rand.nextInt(900000) + 100000L;
                int otpnum = otpNumber.intValue();
                OTP otp = OTP.builder()
                        .number(otpnum)
                        .customer(customer)
                        .driver(car.getDriver())
                        .build();
                otpRepository.save(otp);
                return otpNumber;
            }
        }
        throw  new OTPnotAvailableException("not yet received");
    }


    public CustomerCarResponse covertToCarDto(Car car){
        CustomerCarResponse customerCarResponse = modelMapper.map(car,CustomerCarResponse.class);
        return customerCarResponse;
    }
    public CustomerDTO convertToCustomerDto(Customer customer) throws DriverIsNotAvailabileException {
        CustomerDTO customerDTO = modelMapper.map(customer,CustomerDTO.class);
        List<Car> carList = customer.getCustomerCarList();
        if(carList==null)
            customerDTO.setCarDTOList(new ArrayList<>());
        else{
            List<CarDTO> carDTOList = new ArrayList<>();
            for (Car car1 : carList) {
                CarDTO carDTO = modelMapper.map(car1, CarDTO.class);
                if (car1.getDriver() == null)
                    throw new DriverIsNotAvailabileException("No drivers are free");
                else
                    carDTO.setDriver_id(car1.getDriver().getId());
                carDTOList.add(carDTO);
            }
            customerDTO.setCarDTOList(carDTOList);
        }
       return customerDTO;

    }
    public Customer findCustomer(int customerId) throws CustomerNotFoundException {
        return this.customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("not found"));

    }
}
