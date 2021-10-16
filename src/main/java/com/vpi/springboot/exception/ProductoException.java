package com.vpi.springboot.exception;

public class ProductoException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public ProductoException(String mensaje) {
		super(mensaje);
	}
	
	public static String NotFoundExceptionNombre(String nombre) {
		return "Producto " + nombre + " no se pudo encontrar.";
	}
	
	public static String NotFoundExceptionId(int id) {
		return "Producto " + id + " no se pudo encontrar.";
	}
	
	public static String ProductoYaExiste(String nombre) {
		return "Producto " + nombre + " ya existe en el sistema.";
	}
}
