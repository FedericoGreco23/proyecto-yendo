package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="pedidos")
public class DTProductoVendido implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String _id;
	private String nombreProducto;
	private String nombreRestaurante;
	private int cantidad;
	private String categoria;
	private String fecha;
	
	public DTProductoVendido() {
		// TODO Auto-generated constructor stub
	}
	
	public DTProductoVendido(String idProducto, String nombreProducto, String nombreRestaurante, 
			int cantidad,  String categoria, String fecha) {
		super();
		this._id = idProducto;
		this.nombreProducto = nombreProducto;
		this.nombreRestaurante = nombreRestaurante;
		this.cantidad = cantidad;
		this.categoria = categoria;
		this.fecha = fecha;
	}

//-----------------------------------------------------------------------
	
	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getNombreProducto() {
		return nombreProducto;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	public String getNombreRestaurante() {
		return nombreRestaurante;
	}

	public void setNombreRestaurante(String nombreRestaurante) {
		this.nombreRestaurante = nombreRestaurante;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

}
