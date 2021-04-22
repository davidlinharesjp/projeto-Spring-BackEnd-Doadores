package com.davidlinhares.medicalcheck.entity.enumeration;

import java.util.Arrays;

public enum SexEnum {

	MALE("Masculino"),
	FEMALE("Feminino");

	private String text;

	SexEnum(String text) {
		this.text = text;
	}

	public String getValue() {
		return text;
	}

	public static SexEnum fromString(String text) {
		
        return Arrays.stream(values())
                .filter(sex -> sex.text.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Valor desconhecido: " + text));
	}
	
}
