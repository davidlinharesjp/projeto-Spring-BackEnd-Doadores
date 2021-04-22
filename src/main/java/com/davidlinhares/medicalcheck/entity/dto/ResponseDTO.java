package com.davidlinhares.medicalcheck.entity.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ResponseDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private List<DetailsDonorsDTO> averageDonorImcByAge  = new ArrayList<>();
	private List<DetailsDonorsDTO> averageDonorAgeByBloodType = new ArrayList<>();
	private List<DetailsDonorsDTO> donorsByState = new ArrayList<>();
	private List<DetailsDonorsDTO> possibleDonorsByBloodType = new ArrayList<>();
	private Double percentageOfObeseMen;
	private Double percentageOfObeseWomen;
 
	
	
	

}
