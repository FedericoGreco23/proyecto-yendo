package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;

import com.vpi.springboot.Modelo.Categoria;

public class DTCategoria implements Serializable {
	private static final long serialVersionUID = 1L;

	private String nombre;

	public DTCategoria() {
		super();
	}

	public DTCategoria(String nombre) {
		this.nombre = nombre;
	}
	
	public DTCategoria(Categoria cat) {
		this.nombre = cat.getNombre();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
