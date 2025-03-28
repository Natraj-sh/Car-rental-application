package com.example.carapplication.service;


import com.example.carapplication.dao.CarRepository;
import com.example.carapplication.dao.DriverRepository;
import com.example.carapplication.dao.LocationRepository;
import com.example.carapplication.dto.CarDTO;
import com.example.carapplication.dto.CarRequest;
import com.example.carapplication.exceptions.CarNotFoundException;
import com.example.carapplication.exceptions.DriverIsNotAvailabileException;
import com.example.carapplication.exceptions.DriverNotFoundException;
import com.example.carapplication.exceptions.LocationNotFoundException;
import com.example.carapplication.model.Car;
import com.example.carapplication.model.Location;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class CarService implements CarServiceInterface{
    @Autowired
    CarRepository carRepository;
   @Autowired
    DriverRepository driverRepository;

   @Autowired
    LocationRepository locationRepository;

   @Autowired
    ModelMapper modelMapper;

   @Override
    public CarDTO addCar(CarRequest car) throws DriverNotFoundException, DriverIsNotAvailabileException {
       //Optional<Driver> driver = driverRepository.findById(car.getDriver().getId());

     // if(driver.isPresent())
       Random random = new Random();
       Car carObj = new Car();
        carObj = modelMapper.map(car,Car.class);
        carObj.setLocations(car.getLocation().stream()
                .map(location-> {
                    try {
                        return locationRepository.findByLocationName(location).orElseThrow(()->new LocationNotFoundException("not availabile"));
                    } catch (LocationNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }).toList());
       Car finalCarObj = carObj;

       carObj.getLocations().stream()
               .map(locationObj -> {
//                   Location locationObj =  locationRepository.findByLocationName(location).orElseThrow(()->new LocationNotFoundException("not availabile"));
                   List<Car> locationCarList = locationObj.getCars();
                   if(locationCarList.isEmpty())
                       locationCarList = new ArrayList<>();
                   locationCarList.add(finalCarObj);
                   locationObj.setCars(locationCarList);
                   this.locationRepository.save(locationObj);
                   return locationObj;
               });
       this.carRepository.save(finalCarObj);
      // carObj.setId(random.nextInt(10000));
       return convertToDto(finalCarObj);

       //return new CarDTO(car.getId(),car.getMake(),car.getName(),car.getColor(),car.getDriver().getId(),car.getPrice());
       //throw new DriverNotFoundException("The driver you entered is not present");
   }

    @Override
    public List<CarDTO> getCars() {
       //List<Car> carList= this.carRepository.findAll();
//       List<CarDTO> carDTOList= new ArrayList<>();
//       for(Car car:carList){
//           CarDTO carDTO = new CarDTO(car.getId(),car.getMake(),car.getName(),car.getColor(),car.getDriver().getId(),car.getPrice());
//           carDTOList.add(carDTO);
//       }


        List<CarDTO> carDTOList = carRepository.findAll().stream()
                .map(car -> {
                    try {
                        return convertToDto(car);
                    } catch (DriverIsNotAvailabileException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();

           return carDTOList;
    }

    @Override
    public List<CarDTO> getCarsByMake(String make) {
       List<Car> carList = this.carRepository.findByMake(make);
      return carList.stream().map(car -> {
          try {
              return convertToDto(car);
          } catch (DriverIsNotAvailabileException e) {
              throw new RuntimeException(e);
          }
      }).toList();
    }

    @Override
    public List<CarDTO> getCarsByPrice(int price) {
        List<Car> carList = this.carRepository.findByPrice(price);
        return carList.stream().map(car -> {
            try {
                return convertToDto(car);
            } catch (DriverIsNotAvailabileException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    @Override
    public CarDTO setLocation(int carId, int locationId) throws CarNotFoundException, LocationNotFoundException, DriverIsNotAvailabileException {
       // Optional<Location> location = locationRepository.findById(locationId);
        Car car = carRepository.findById(carId).orElseThrow(()->new CarNotFoundException("not registered car"));
        Location locationObj = locationRepository.findById(locationId).orElseThrow(() ->new LocationNotFoundException("location not registered"));
        List<Location> locationList = car.getLocations();
        List<Car> carList = locationObj.getCars();
        if(car.getLocations().isEmpty())
            locationList = new ArrayList<>();
        if(locationObj.getCars().isEmpty()) {
            carList = new ArrayList<>();
        }
        locationList.add(locationObj);
        carList.add(car);
        car.setLocations(locationList);
        locationObj.setCars(carList);
       this.carRepository.save(car);
       this.locationRepository.save(locationObj);
        return convertToDto(car);
    }

    @Override
    public List<CarDTO> getCarsBasedOnLocation(String locationName) throws LocationNotFoundException {
//       Location locationObj =this.locationRepository.findByLocationName(locationName).orElseThrow(()->new LocationNotFoundException("locatin not found"));
//       List<Car> carList = this.carRepository.findByLocation(locationObj);
//       return carList.stream().map(car -> {
//           try {
//               return convertToDto(car);
//           } catch (DriverIsNotAvailabileException e) {
//               throw new RuntimeException(e);
//           }
        List<Car> carList12 = this.carRepository.findAll().stream()
                .filter(car -> {
                    Boolean check = car.getLocations().stream()
                            .anyMatch(location -> location.getLocationName().equalsIgnoreCase(locationName));
                    if(check)
                        return true;
                    return false;
                }).toList();
//       }).toList();
//        List<Car> carList= carRepository.findAll().stream()
//                .map(car ->{
//                    List<Location> locations =
//                            car.getLocations().stream()
//                             .filter(location ->location.getLocationName().equalsIgnoreCase(locationName)
//                             ).toList();
//                      if(!locations.isEmpty())
//                          return car;
//                      else
//                          return null;
//                }).toList();
        //can do this to avoid with checking for the null
        //first streaming the carlist and filter it based on condition now inside lamda we are getting the list of location t
        //then we are streaming that list and filtering based on equal name then return the value now map the car to cardto
        //convert to list return
        List<CarDTO> carList1 = carRepository.findAll().stream()
                .filter(car -> {
                    List<Location> locationList = car.getLocations().stream()
                            .filter(location -> location.getLocationName().equalsIgnoreCase(locationName)).toList();
                    if(locationList.isEmpty())
                         return false;
                    else
                        return true;
                }).map(car -> {
                    try {
                        return convertToDto(car);
                    } catch (DriverIsNotAvailabileException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();

        return carList1;
        // List<Car> locationCarList= carList.stream().filter(car -> car!=null).toList();
//        List<CarDTO> carDTOList = carList.stream().filter(car -> car!=null)
//                 .map(car -> convertToDto(car)).toList();
//        return carDTOList;
    }
    public CarDTO convertToDto(Car car) throws DriverIsNotAvailabileException {
       CarDTO carDTO = modelMapper.map(car, CarDTO.class);
       if(car.getDriver()!=null)
       carDTO.setDriver_id(car.getDriver().getId());
       else
           carDTO.setDriver_id(0);
       return carDTO;
    }
}
