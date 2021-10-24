package com.vpi.springboot.exception;

public class PedidoException extends Exception {


	private static final long serialVersionUID = 1L;
	
	public PedidoException(String mensaje) {
		super(mensaje);
	}
	
	public static String NotFoundExceptionId(int id) {
		return "Pedido " + id + " no se pudo encontrar.";
	}
	
	public static String NotValidId() {
		return "Inserte un id valido de pedido.";
	}

}
