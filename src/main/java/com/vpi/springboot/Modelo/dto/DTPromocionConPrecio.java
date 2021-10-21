package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Promocion;


public class DTPromocionConPrecio implements Serializable {
	private static final long serialVersionUID = 1L;


    private List<DTProductoIdCantidad> productos;// id producto, cantidad
    private Integer precio; 
    private Integer descuento; 
    private String nombre;
    private String descripcion;
    private String foto;
	
	public DTPromocionConPrecio() {
		super();
	}

	public List<DTProductoIdCantidad> getProductos() {
		return productos;
	}

	public void setProductos(List<DTProductoIdCantidad> productos) {
		this.productos = productos;
	}

	public Integer getPrecio() {
		return precio;
	}

	public void setPrecio(Integer precio) {
		this.precio = precio;
	}

	public Integer getDescuento() {
		return descuento;
	}

	public void setDescuento(Integer descuento) {
		this.descuento = descuento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	


}