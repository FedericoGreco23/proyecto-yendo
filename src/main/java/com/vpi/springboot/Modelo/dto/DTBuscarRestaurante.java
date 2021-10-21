package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;

public class DTBuscarRestaurante implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String nombre;
	private String foto;
	private String direccion;
	
	
	public DTBuscarRestaurante() {
		// TODO Auto-generated constructor stub
	}

	public DTBuscarRestaurante(String nombre, String foto, String direccion) {
		super();
		this.nombre = nombre;
		this.foto = foto;
		this.direccion = direccion;
	}

	
//----------------------GETTERS Y SETTERS---------------------------------------------------------
	
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
}
