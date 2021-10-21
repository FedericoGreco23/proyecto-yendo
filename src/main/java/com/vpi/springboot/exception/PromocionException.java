package com.vpi.springboot.exception;

public class PromocionException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public PromocionException(String mensaje) {
		super(mensaje);
	}
	
	public static String NotFoundExceptionNombre(String nombre) {
		return "Promocion " + nombre + " no se pudo encontrar.";
	}
	
	public static String NotFoundExceptionId(int id) {
		return "Promocion " + id + " no se pudo encontrar.";
	}
	
	public static String PromocionYaExiste(String nombre) {
		return "Promocion " + nombre + " ya existe en el sistema.";
	}

	public static String NotFoundException() {
		return "Error al obtener los productos de la pormocion";
	}
}
