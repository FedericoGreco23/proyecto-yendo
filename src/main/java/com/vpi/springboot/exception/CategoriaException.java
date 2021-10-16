
package com.vpi.springboot.exception;

public class CategoriaException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public CategoriaException(String mensaje) {
		super(mensaje);
	}
	
	public static String NotFoundException(String nombre) {
		return "Categoria " + nombre + " no se pudo encontrar.";
	}
	
	public static String CategoriaYaExiste(String nombre) {
		return "Categoria " + nombre + " ya existe.";
	}
}
