package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;

public class DTProductoIdCantidad implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer cantidad;
	public DTProductoIdCantidad() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	
	
}
