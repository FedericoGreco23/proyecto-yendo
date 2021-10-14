package com.vpi.springboot.exception;

public class RestauranteException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public RestauranteException(String mensaje) {
		super(mensaje);
	}
	
	public static String MailYaExiste(String mail) {
		return "Un restaurante con mail " + mail + " ya existe";
	}
	
	public static String NotFoundException(String mail) {
		return "Restaurante de mail " + mail + " no se pudo encontrar";
	}
	
	public static String RestauranteYaExiste(String nombre) {
		return "Un restaurante con nombre " + nombre + " ya existe";
	}

}
