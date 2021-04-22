package com.davidlinhares.medicalcheck.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.davidlinhares.medicalcheck.entity.Donor;
import com.davidlinhares.medicalcheck.repository.DonorRepository;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

	@Autowired
	private DonorRepository donorRepository;

	@Override
	public void run(String... args) throws Exception {

		
		  Donor dono = new Donor();
		  
		  
		  donorRepository.save(dono);
		 
	}

}
