package com.vpi.springboot.Modelo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Categoria {

	@Id
	private String nombre;
	private String foto;

	public Categoria() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Categoria(String nombre, String foto) {
		super();
		this.nombre = nombre;
		this.foto = foto;
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
}