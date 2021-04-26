package com.davidlinhares.medicalcheck.resource.exception;

import java.io.IOException;

public class IlegalFormatFile extends IOException {

	private static final long serialVersionUID = 1L;
	public IlegalFormatFile(String msg) {
		super(msg);
	}
}
