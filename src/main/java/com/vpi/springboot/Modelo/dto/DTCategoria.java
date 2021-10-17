package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;

import com.vpi.springboot.Modelo.Categoria;

public class DTCategoria implements Serializable {
	private static final long serialVersionUID = 1L;

	private String nombre;
	private String foto;

	public DTCategoria() {
		super();
	}

	public DTCategoria(String nombre, String foto) {
		this.nombre = nombre;
		this.foto = foto;
	}
	
//----------------------GETTERS Y SETTERS---------------------------------------------------------
	
	public DTCategoria(Categoria cat) {
		this.nombre = cat.getNombre();
	}

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
}
