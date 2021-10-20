package com.vpi.springboot.exception;

public class DireccionException extends Exception {


	private static final long serialVersionUID = 1L;
	
	public DireccionException(String mensaje) {
		super(mensaje);
	}
	
	public static String NotFoundExceptionId(int id) {
		return "Direccion " + id + " no se pudo encontrar.";
	}
}