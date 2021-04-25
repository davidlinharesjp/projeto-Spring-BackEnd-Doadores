package com.davidlinhares.medicalcheck.service.exception;

import java.util.ArrayList;
import java.util.List;

public class IntegrationViolationDataBaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public IntegrationViolationDataBaseException(String msg, List<String> erros) {
		super("Dados duplicado em banco: " + msg );
		this.erros.addAll(erros);
	}

	private List<String> erros = new ArrayList<>();

	public List<String> getErros() {
		return this.erros;
	}
}
