package com.vpi.springboot.exception;

public class PermisosException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public PermisosException(String mensaje) {
		super(mensaje);
	}
	
	
	public static String NoPermisosException (String userTypeRequerido) {
		return "El acceso está restringido para usuarios "+userTypeRequerido;
	}
}
