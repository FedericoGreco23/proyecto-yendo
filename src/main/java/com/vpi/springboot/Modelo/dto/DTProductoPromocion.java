package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;

import com.vpi.springboot.Modelo.Producto;

public class DTProductoPromocion implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String nombre;
	private Integer cantidad;
	private String categoria;

	public DTProductoPromocion() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DTProductoPromocion(Producto producto) {
		super();
		this.id = producto.getId();
		this.nombre = producto.getNombre();
		this.categoria = producto.getCategoria().getNombre();
		this.cantidad = 1;
	}
	
	public DTProductoPromocion(Producto producto, int cantidad) {
		super();
		this.id = producto.getId();
		this.nombre = producto.getNombre();
		this.categoria = producto.getCategoria().getNombre();
		this.cantidad = cantidad;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	
	public void addCantidad() {
		this.cantidad++;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
}