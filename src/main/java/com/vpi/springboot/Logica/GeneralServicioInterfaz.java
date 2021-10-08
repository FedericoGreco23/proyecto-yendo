package com.vpi.springboot.Logica;

import java.util.List;

import com.vpi.springboot.exception.UsuarioException;

public interface GeneralServicioInterfaz {
	public String iniciarSesion(String mail, String password) throws UsuarioException;
	
	public List<String> listarUsuariosRegistrados();
}