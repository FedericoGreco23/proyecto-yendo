package com.vpi.springboot.exception;

public class TokenException extends Exception {

	private static final long serialVersionUID = 1L;

	public TokenException(String mensaje) {
		super(mensaje);
	}

	public static String NotFoundException() {
		return "Ha ocurrido un error activando su cuenta. Por favor comuníquese con nosotros para resolver el problema.";
	}

	public static String MailError() {
		return "Ha ocurrido un error al mandar el mail. Por favor comuníquese con nosotros para resolver el problema.";
	}

	public static String ExpiredToken() {
		return "Su link de activación ha expirado. Se ha enviado un nuevo mail de verificación. "
				+ "En caso de seguir teniendo problemas, por favor comuníquese con nosotros. ";
	}
}
