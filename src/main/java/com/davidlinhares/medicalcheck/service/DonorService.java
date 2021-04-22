package com.davidlinhares.medicalcheck.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davidlinhares.medicalcheck.entity.Donor;
import com.davidlinhares.medicalcheck.entity.dto.DetailsDonorsDTO;
import com.davidlinhares.medicalcheck.entity.dto.ResponseDTO;
import com.davidlinhares.medicalcheck.entity.enumeration.SexEnum;
import com.davidlinhares.medicalcheck.repository.DonorRepository;

@Service
public class DonorService {

	private static final String[] BLOODTYPES = { "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-" };
	private static final String[] STATES = { "AC", "AL", "AP", "AM", "BA", "CE", "ES", "GO", "MA", "MT", "MS", "MG",
			"PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO", "DF" };
	private static final String[] GROUPSBYAGES = { "0a10", "11a20", "21a30", "31a40", "41a50", "51a60", "61a69" };

	/*
	 * private static final Map<String, String[]> TYPERECIPIENTBLOODS = new
	 * HashMap<String, String[]>() { private static final long serialVersionUID =
	 * 1L; { put("A+", new String[] { "A+", "A-", "O+", "O-" }); put("A-", new
	 * String[] { "A-", "0-" }); put("B+", new String[] { "B+", "B-", "O+", "O-" });
	 * put("B-", new String[] { "B-", "O-" }); put("AB+", new String[] { "A+", "B+",
	 * "O+", "AB+", "A-", "B-", "O-", "AB-" }); put("AB-", new String[] { "A-",
	 * "B-", "0-", "AB-" }); put("O+", new String[] { "0+", "0-" }); put("O-", new
	 * String[] { "0-" }); } };
	 */

	@Autowired
	DonorRepository repository;

	public ResponseDTO insertJson(List<Donor> donors) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (!donors.isEmpty()) {
			try {
				repository.saveAll(donors);
			} catch (Exception e) {
				e.printStackTrace();
			}
			responseDTO = convertDonorsDTO(donors);
		}
		return responseDTO;
	}

	public ResponseDTO getDTOs() {
		List<Donor> donors = repository.findAll();
		return convertDonorsDTO(donors);
	}

	public ResponseDTO insert(Donor donor) {
		repository.save(donor);
		return getDTOs();
	}

	public ResponseDTO convertDonorsDTO(List<Donor> donors) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setDonorsByState(findDonorsByState(donors));
		responseDTO.setAverageDonorImcByAge(findGroupByAgeAndIMC(donors));
		responseDTO.setPercentageOfObeseMen(checkPercentegeOfObese(donors, SexEnum.MALE));
		responseDTO.setPercentageOfObeseWomen(checkPercentegeOfObese(donors, SexEnum.FEMALE));
		responseDTO.setAverageDonorAgeByBloodType(averageAgeForBloodType(donors));
		responseDTO.setPossibleDonorsByBloodType(numberDonorsByBloodType(donors));
		return responseDTO;
	}

	public List<Donor> findAllDonor() {
		return repository.findAll();
	}

	public List<DetailsDonorsDTO> findDonorsByState(List<Donor> listDonor) {
		List<Donor> donorsByState;
		List<DetailsDonorsDTO> list = new ArrayList<>();
		DetailsDonorsDTO detailsDonorsDTO;
		for (String state : STATES) {
			donorsByState = listDonor.stream().filter(donor -> donor.getEstado().equals(state))
					.collect(Collectors.toList());
			if (!donorsByState.isEmpty() && donorsByState.size() > 0) {
				detailsDonorsDTO = new DetailsDonorsDTO();
				detailsDonorsDTO.setDescription(state);
				detailsDonorsDTO.setTotal((long) donorsByState.size());
				// detailsDonorsDTO.setDonors(donorsByState);
				list.add(detailsDonorsDTO);
			}
		}
		return list;
	}

	public List<DetailsDonorsDTO> findGroupByAgeAndIMC(List<Donor> donors) {
		List<DetailsDonorsDTO> listDetailsDonorsDTOs = new ArrayList<>();
		for (String groupAge : GROUPSBYAGES) {
			List<Donor> list = new ArrayList<>();
			DetailsDonorsDTO detailsDonorsDTO = new DetailsDonorsDTO();
			Long ageMin = Long.parseLong(groupAge.substring(0, groupAge.indexOf("a")));
			Long ageMax = Long.parseLong(groupAge.substring(groupAge.indexOf("a") + 1, groupAge.length()));
			BigDecimal totalBig = new BigDecimal("0.0");
			for (Donor donor : donors) {
				Integer age = donor.getAge();
				if (age != null && age >= ageMin && age <= ageMax) {
					list.add(donor);
					detailsDonorsDTO.setDescription(groupAge);
					String imc = donor.getIMC().toString();
					totalBig = totalBig.add(new BigDecimal(imc));
				}
			}
			if (!list.isEmpty() && list.size() > 0) {
				detailsDonorsDTO.setTotal((long) list.size());
				detailsDonorsDTO.setMedia(calcAvg(totalBig.doubleValue(), (long) list.size()));
				detailsDonorsDTO.getDonors().addAll(list);
				listDetailsDonorsDTOs.add(detailsDonorsDTO);
			}
		}

		return listDetailsDonorsDTOs;
	}

	public Double checkPercentegeOfObese(List<Donor> donors, SexEnum sexo) {
		if (!donors.isEmpty() && sexo != null) {
			Long amountDonorBySex = donors.stream().filter(donor -> donor.getSexo().equals(sexo.getValue())).count();
			Long amountDonorObese = donors.stream()
					.filter(donor -> donor.getSexo().equals(sexo.getValue()) && donor.isObese()).count();
			BigDecimal bigAmountDonorObese = new BigDecimal(amountDonorObese.toString());
			return bigAmountDonorObese.multiply(new BigDecimal("100"))
					.divide(new BigDecimal(amountDonorBySex), 2, RoundingMode.HALF_UP).doubleValue();
		}
		return null;
	}

	public List<DetailsDonorsDTO> averageAgeForBloodType(List<Donor> donors) {
		List<DetailsDonorsDTO> list = new ArrayList<>();
		List<Donor> listDonorsBloodType;

		for (String typeBlood : BLOODTYPES) {
			DetailsDonorsDTO detailsDonorsDTO = new DetailsDonorsDTO();
			AtomicInteger atomicInteger = new AtomicInteger(0);
			listDonorsBloodType = donors.stream().filter(donor -> donor.getTipo_sanguineo().equalsIgnoreCase(typeBlood))
					.collect(Collectors.toList());
			if (!listDonorsBloodType.isEmpty()) {
				listDonorsBloodType.stream().map(doner -> (int) doner.getAge())
						.forEach(i -> atomicInteger.addAndGet(i.intValue()));
				detailsDonorsDTO.setDescription(typeBlood);
				detailsDonorsDTO.setTotal((long) listDonorsBloodType.size());
				detailsDonorsDTO.setMedia(calcAvg(atomicInteger.doubleValue(), (long) listDonorsBloodType.size()));
				// detailsDonorsDTO.getDonors().addAll(listDonorsBloodType);
				list.add(detailsDonorsDTO);
			}
		}
		return list;
	}

	public Double calcAvg(Double dividendo, Long divisor) {
		if (divisor != null && dividendo != null) {
			BigDecimal dividendoBig = new BigDecimal(dividendo.toString());
			return dividendoBig.divide(new BigDecimal(divisor.toString()), 2, RoundingMode.HALF_UP).doubleValue();

		}
		return null;
	}

	public List<DetailsDonorsDTO> numberDonorsByBloodType(List<Donor> donors) {
		List<DetailsDonorsDTO> detailsDonorsDTOs = new ArrayList<>();
		for (String blood : BLOODTYPES) {
			List<Donor> listDonor = new ArrayList<>();
			DetailsDonorsDTO detailsDonorsDTO = new DetailsDonorsDTO();
			switch (blood) {
			case "A+":
				listDonor = donors.stream()
						.filter(donor -> donor.isEligibleForDonation() && (donor.getTipo_sanguineo().equals("A+")
								|| donor.getTipo_sanguineo().equals("A-") || donor.getTipo_sanguineo().equals("O+")
								|| donor.getTipo_sanguineo().equals("O-")))
						.collect(Collectors.toList());
				break;
			case "A-":
				listDonor = donors.stream()
						.filter(donor -> donor.isEligibleForDonation()
								&& (donor.getTipo_sanguineo().equals("A-") || donor.getTipo_sanguineo().equals("O-")))
						.collect(Collectors.toList());
				break;
			case "B+":
				listDonor = donors.stream()
						.filter(donor -> donor.isEligibleForDonation() && (donor.getTipo_sanguineo().equals("B+")
								|| donor.getTipo_sanguineo().equals("B-") || donor.getTipo_sanguineo().equals("O+")
								|| donor.getTipo_sanguineo().equals("O-")))
						.collect(Collectors.toList());
				break;
			case "B-":
				listDonor = donors.stream()
						.filter(donor -> donor.isEligibleForDonation()
								&& (donor.getTipo_sanguineo().equals("B-") || donor.getTipo_sanguineo().equals("O-")))
						.collect(Collectors.toList());
				break;
			case "AB+":
				listDonor = donors.stream()
						.filter(donor -> donor.isEligibleForDonation() && (donor.getTipo_sanguineo().equals("A+")
								|| donor.getTipo_sanguineo().equals("A-") || donor.getTipo_sanguineo().equals("O+")
								|| donor.getTipo_sanguineo().equals("O-") || donor.getTipo_sanguineo().equals("B+")
								|| donor.getTipo_sanguineo().equals("B-") || donor.getTipo_sanguineo().equals("AB+")
								|| donor.getTipo_sanguineo().equals("AB-")))
						.collect(Collectors.toList());
				break;
			case "AB-":
				listDonor = donors.stream()
						.filter(donor -> donor.isEligibleForDonation() && (donor.getTipo_sanguineo().equals("A-")
								|| donor.getTipo_sanguineo().equals("B-") || donor.getTipo_sanguineo().equals("O-")
								|| donor.getTipo_sanguineo().equals("AB-")))
						.collect(Collectors.toList());
				break;
			case "O+":
				listDonor = donors.stream()
						.filter(donor -> donor.isEligibleForDonation()
								&& (donor.getTipo_sanguineo().equals("O-") || donor.getTipo_sanguineo().equals("O+")))
						.collect(Collectors.toList());
				break;
			case "O-":
				listDonor = donors.stream()
						.filter(donor -> donor.isEligibleForDonation() && (donor.getTipo_sanguineo().equals("O-")))
						.collect(Collectors.toList());
				break;

			default:
				break;
			}

			if (!listDonor.isEmpty() && listDonor.size() > 0) {
				detailsDonorsDTO.setDescription(blood);
				detailsDonorsDTO.setTotal((long) listDonor.size());
				// detailsDonorsDTO.getDonors().addAll(listDonor);
				detailsDonorsDTOs.add(detailsDonorsDTO);
			}

		}
		return detailsDonorsDTOs;
	}

}
