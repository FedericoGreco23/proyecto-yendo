package com.vpi.springboot.Modelo;


import javax.persistence.Entity;

@Entity
public class Administrador extends Usuario{
	
	public Administrador() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Administrador(String mail, String contrasenia, String telefono, String foto, Boolean bloqueado, 
			Boolean activo, String nickname) {
		super(mail, contrasenia, telefono, foto, bloqueado, activo);
	}
	
	

}
