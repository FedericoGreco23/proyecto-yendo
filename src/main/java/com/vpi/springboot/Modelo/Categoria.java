package com.vpi.springboot.Modelo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Categoria {

	@Id
	private String nombre;
	

	public Categoria() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Categoria(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}




	
	
}