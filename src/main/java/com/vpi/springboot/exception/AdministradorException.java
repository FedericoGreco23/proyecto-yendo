package com.vpi.springboot.exception;

public class AdministradorException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public AdministradorException(String mensaje) {
		super(mensaje);
	}
	
	public static String NotFoundException(String mail) {
		return "Administrador de mail " + mail + " no se pudo encontrar.";
	}
	
	public static String AdministradorYaExiste(String mail) {
		return "Administrador " + mail + " ya existe.";
	}
}