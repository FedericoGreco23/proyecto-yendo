package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;

import com.vpi.springboot.Modelo.Administrador;

public class DTAdministrador extends DTUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	public DTAdministrador() {
		super();
	}

	public DTAdministrador(String mail, String contrasenia, String telefono, String foto, Boolean bloqueado,
			Boolean activo) {
		super(mail, contrasenia, telefono, foto, bloqueado, activo);
	}

	public DTAdministrador(Administrador user) {
		super(user.getMail(), user.getContrasenia(), user.getTelefono(), user.getFoto(), user.getBloqueado(),
				user.getActivo());
	}
}
