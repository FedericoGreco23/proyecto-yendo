package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

public class DTTopCategoria implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String categoria;
	private int cantidad;
	
	public DTTopCategoria() {
		// TODO Auto-generated constructor stub
	}
	
	public DTTopCategoria(String categoria, int cantidad) {
		super();
		this.categoria = categoria;
		this.cantidad = cantidad;
	}
	
//-----------------------------------------------------------------------

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	

}
