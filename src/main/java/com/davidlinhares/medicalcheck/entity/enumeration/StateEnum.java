package com.davidlinhares.medicalcheck.entity.enumeration;

import java.util.Arrays;

public enum StateEnum {
	
	ACRE("AC"),
	ALAGOA("AL"),
	AMAPA("AP"),
	AMAZONAS("AM"),
	BAHIA("BA"),
	CEARA("CE"),
	ESPIRITOSANTO("ES"),
	GOIAS("GO"),
	MARANHAO("MA"),
	MATOGROSSO("MT"),
	MATOGROSSODOSUL("MS"),
	MINASGERAIS("MG"),
	PARA("PA"),
	PARAIBA("PB"),
	PARANA("PR"),
	PERNAMBUCO("PE"),
	PIAUI("PI"),
	RIODEJANIERO("RJ"),
	RIOGRANDEDONORTE("RN"),
	RIOGRANDEDOSUL("RS"),
	RONDONIA("RO"),
	RORAIMA("RR"),
	SANTACATARINA("SC"),
	SAOPAULO("SP"),
	SERGIPE("SE"),
	TOCANTINS("TO"),
	DISTRITOFEDERAL("DF");
	
	private String text;
	
	private StateEnum(String text) {
		this.text = text;
	}
	public String getValeu() {
		return text; 
	}
	
	public static StateEnum fromString(String text){
		return Arrays.stream(values())
				.filter(state -> state.text.equalsIgnoreCase(text))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Valor desconhecido" + text));
	}
	
}
