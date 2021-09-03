package com.vpi.springboot.exception;

public class PedidosException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public PedidosException(String mensaje) {
		super(mensaje);
	}
	
	public static String NotFoundException(String id) {
		return "Pedido de id " + id + " no se pudo encontrar";
	}
	
	public static String PedidoYaExiste() {
		return "Pedido ya existe";
	}
}
