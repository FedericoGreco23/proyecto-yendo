package com.vpi.springboot.Logica;

import com.vpi.springboot.exception.UsuarioException;

public interface GeneralServicioInterfaz {
	public String iniciarSesion(String mail, String password) throws UsuarioException;
}