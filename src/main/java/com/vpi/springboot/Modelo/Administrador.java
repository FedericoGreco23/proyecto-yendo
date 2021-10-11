package com.vpi.springboot.Modelo;


import java.time.LocalDate;

import javax.persistence.Entity;

import com.vpi.springboot.Modelo.dto.DTAdministrador;

@Entity
public class Administrador extends Usuario{
	
	public Administrador() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Administrador(String mail, String contrasenia, String telefono, String foto, Boolean bloqueado, 
			Boolean activo, LocalDate fechaCreacion) {
		super(mail, contrasenia, telefono, foto, bloqueado, activo, fechaCreacion);
	}
	
	//Funcion para buscarUsuario y otros
	public DTAdministrador getDatos() {
		return new DTAdministrador(this.getMail(), this.getFoto(), this.getFechaCreacion());
	}

}
