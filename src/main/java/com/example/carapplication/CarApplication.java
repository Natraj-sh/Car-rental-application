package com.example.carapplication;

import com.example.carapplication.dao.LocationRepository;
import com.example.carapplication.model.Location;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication

public class CarApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(LocationRepository locationRepository) {
		return args -> {
			Location location1=new Location(59,"madurai");
			Location location2=new Location(72,"tirunelveli");
			locationRepository.save(location1);
			locationRepository.save(location2);
		};
//		//return new CommandLineRunner(){
		//implementing the commanlinerunner interface using a internal class and then
		//overinding the run method and do what we want to do
//		@overide
//				public void run(String...args){
//
//		}
//	//}
	}

}
