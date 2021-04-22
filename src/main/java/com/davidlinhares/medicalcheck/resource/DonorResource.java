package com.davidlinhares.medicalcheck.resource;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.davidlinhares.medicalcheck.entity.Donor;
import com.davidlinhares.medicalcheck.entity.dto.ResponseDTO;
import com.davidlinhares.medicalcheck.service.DonorService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
@RequestMapping(value = "/donor")
public class DonorResource {

	@Autowired
	private DonorService donorService;

	@PostMapping
	public ResponseEntity<ResponseDTO> insertJson(@RequestBody String json) throws IOException {

		ObjectMapper mapper = new ObjectMapper();

//		  LocalDate df = new SimpleDateFormat("yyyy-MM-dd");
		// DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
		mapper.setDateFormat(date);

		List<Donor> donorsList = mapper.readValue(json, new TypeReference<List<Donor>>() {
		});

		ResponseDTO dto = donorService.insertJson(donorsList);
		return ResponseEntity.ok().body(dto);
	}

	@GetMapping()
	public ResponseEntity<ResponseDTO> getDonor() {
		ResponseDTO dto = donorService.getDTOs();
		return ResponseEntity.ok().body(dto);
	}

	@PostMapping(value = "/insert")
	public ResponseEntity<ResponseDTO> insertDonor(@RequestBody Donor donor) {
		ResponseDTO dto = donorService.insert(donor);
		return ResponseEntity.ok().body(dto);
	}

}
