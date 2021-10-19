
package com.vpi.springboot.exception;

public class ReclamoException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public ReclamoException(String mensaje) {
		super(mensaje);
	}
	
	public static String TooOldPedido(Integer idPedido) {
		return "El pedido " + idPedido.toString() + " fue realizado hace más de 24 horas.";
	}

	public static String PedidoNotFound(Integer idPedido) {
		return "El pedido " + idPedido.toString() + " no fue encontrado";
	}

	public static String UserPedidoException(Integer idPedido, String mailCliente) {

		return "El pedido " + idPedido.toString() + " no fue realizado por el usuario "+mailCliente;
	}
	

}
