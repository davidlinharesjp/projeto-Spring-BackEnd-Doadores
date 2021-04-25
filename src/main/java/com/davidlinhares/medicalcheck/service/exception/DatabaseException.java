package com.davidlinhares.medicalcheck.service.exception;

import java.util.ArrayList;
import java.util.List;

public class DatabaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private List<String> erros = new ArrayList<>();

	public DatabaseException(String msg, List<String> erros) {
		super(msg);
		this.erros.addAll(erros);
	}

	public List<String> getErros() {
		return this.erros;
	}
}
