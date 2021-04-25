package com.davidlinhares.medicalcheck.service.exception;

import java.util.ArrayList;
import java.util.List;

public class ResourceNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public ResourceNotFoundException (Object id, String msg, List<String> erros) {
		super("Não foi encontrado: " + msg + ": " + id);
		this.erros.addAll(erros);
	}

	private List<String> erros = new ArrayList<>();
	
	public List<String> getErros(){
		return this.erros;
	}
}
