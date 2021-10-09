package com.vpi.springboot.Logica;

import java.util.List;

import com.vpi.springboot.exception.UsuarioException;

public interface GeneralServicioInterfaz {
	public String iniciarSesion(String mail, String password) throws UsuarioException;
	
	public void recuperarPassword(String mail) throws UsuarioException;
	
	public void verificarMail(String mail) throws UsuarioException;

	public void activarCuenta(String mail, String tipo);
	
	public List<String> listarUsuariosRegistrados();
}