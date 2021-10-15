
package com.vpi.springboot.exception;

public class RestauranteException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public RestauranteException(String mensaje) {
		super(mensaje);
	}
	
	public static String NotFoundExceptionMail(String mail) {
		return "Restaurante de mail " + mail + " no se pudo encontrar.";
	}
	
	public static String NotFoundExceptionNombre(String nombre) {
		return "Restaurante de nombre " + nombre + " no se pudo encontrar.";
	}
	
	public static String RestauranteYaExiste(String mail) {
		return "Restaurante " + mail + " ya existe.";
	}
}
