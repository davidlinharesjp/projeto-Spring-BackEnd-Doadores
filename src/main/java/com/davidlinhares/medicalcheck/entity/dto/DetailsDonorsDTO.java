package com.davidlinhares.medicalcheck.entity.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.davidlinhares.medicalcheck.entity.Donor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailsDonorsDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String description;
	private Long total;
	private Double porcentage;
	private Double media;
	private List<Donor> donors = new ArrayList<>();
}
