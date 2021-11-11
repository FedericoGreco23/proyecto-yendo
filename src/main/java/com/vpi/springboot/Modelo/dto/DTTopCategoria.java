package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="pedidos")
public class DTTopCategoria implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String categoria;
	private String cantidad;
	
	public DTTopCategoria() {
		// TODO Auto-generated constructor stub
	}
	
	public DTTopCategoria(String categoria, String cantidad) {
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

	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}
	
	

}
