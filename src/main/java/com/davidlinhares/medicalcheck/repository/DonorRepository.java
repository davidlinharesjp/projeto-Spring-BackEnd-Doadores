package com.davidlinhares.medicalcheck.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.davidlinhares.medicalcheck.entity.Donor;

public interface DonorRepository extends JpaRepository<Donor, String> {

	@Query("SELECT donor FROM Donor donor WHERE donor.estado = :estado")
	List<Donor> findDonorByState(@Param("estado") String estado);
	
	Optional<Donor> findByCpf(String cpf);
	
	
	
	
}
