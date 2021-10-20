package com.vpi.springboot.exception;

public class CarritoException extends Exception {


	private static final long serialVersionUID = 1L;
	
	public CarritoException(String mensaje) {
		super(mensaje);
	}
	
	public static String NotFoundExceptionId(int id) {
		return "Carrito " + id + " no se pudo encontrar.";
	}
}
