package com.vpi.springboot.exception;

public class TokenException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public TokenException(String mensaje) {
		super(mensaje);
	}
	
	public static String NotFoundException() {
		return "Hubo un error activando su cuenta.";
	}
	
	public static String MailError() {
		return "No se pudo mandar mail.";
	}
	
	public static String ExpiredToken() {
		return "El token expiró. Se ha enviado un nuevo mail de verificación.";
	}
}
