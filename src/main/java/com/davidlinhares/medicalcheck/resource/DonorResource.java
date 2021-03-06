package com.davidlinhares.medicalcheck.resource;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.davidlinhares.medicalcheck.entity.Donor;
import com.davidlinhares.medicalcheck.entity.dto.ResponseDTO;
import com.davidlinhares.medicalcheck.resource.exception.IlegalFormatFile;
import com.davidlinhares.medicalcheck.service.DonorService;
import com.fasterxml.jackson.core.JsonParseException;
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
		DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
		mapper.setDateFormat(date);
		List<Donor> donorsList = null;
		try {
			donorsList = mapper.readValue(json, new TypeReference<List<Donor>>() {
			});
		} catch (JsonParseException e) {
			throw new JsonParseException(e.getProcessor(), "Formato Json Invalido:   ", e.getLocation());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ResponseDTO dto = donorService.insertJson(donorsList);
		return ResponseEntity.ok().body(dto);
	}

	@GetMapping()
	public ResponseEntity<ResponseDTO> getDonor() {
		ResponseDTO dto = donorService.getDTOs();
		return ResponseEntity.ok().body(dto);
	}

	@PostMapping(value = "/insert")
	public ResponseEntity<ResponseDTO> insertDonor(@Valid @RequestBody Donor donor) throws MethodArgumentNotValidException {
		ResponseDTO dto = new ResponseDTO();
		dto = donorService.insert(donor);
		
		return ResponseEntity.ok().body(dto);
	}
	
	@PostMapping(value = "/jsonFile")
	public ResponseEntity<ResponseDTO> insertFileJson(@RequestParam MultipartFile file) throws IllegalStateException, IOException{
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		if(!extension.equalsIgnoreCase("JSON")) {
			throw new IlegalFormatFile(extension);
		}
		ObjectMapper mapper = new ObjectMapper();
		DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
		mapper.setDateFormat(date);
		List<Donor> donorsList = null;
		try {
			donorsList = mapper.readValue(file.getInputStream(), new TypeReference<List<Donor>>() {
			});
		} catch (JsonParseException e) {
			throw new JsonParseException(e.getProcessor(), "Formato Json Invalido:   ", e.getLocation());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ResponseDTO dto = donorService.insertJson(donorsList);
		return ResponseEntity.ok().body(dto);
		
	}

}
