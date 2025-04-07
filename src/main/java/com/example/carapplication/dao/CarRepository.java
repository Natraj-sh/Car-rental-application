package com.example.carapplication.dao;
import com.example.carapplication.model.Car;
import com.example.carapplication.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car,Integer> {

    List<Car> findByMake(String make);
    List<Car> findByPrice(int price);
    //List<Car> findByLocation(Location location);
    Optional<Car>  findByName(String name);

    List<Car>  findByNameIn(List<String> name);
    //spring.datasource.url=jdbc:h2:mem:cardb

}
