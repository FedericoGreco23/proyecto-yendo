package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;

public class DTProductoCarrito implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer cantidad;
	private DTProducto producto;

	public DTProductoCarrito(DTProducto producto, Integer cantidad) {
		this.producto = producto;
		this.cantidad = cantidad;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public DTProducto getProducto() {
		return producto;
	}

	public void setProducto(DTProducto producto) {
		this.producto = producto;
	}
	
	
	public void sumarCantidad(int cant) {
		this.cantidad = cantidad + cant;
	}
	
	public void deleteProducto(DTProducto producto) {
		this.producto = null;
	}

	@Override
	public String toString() {
		return "DTProductoCarrito [cantidad=" + cantidad + ", producto=" + producto + "]";
	}
}