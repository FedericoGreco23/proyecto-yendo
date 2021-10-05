package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.util.List;

import com.vpi.springboot.Modelo.Categoria;
import com.vpi.springboot.Modelo.Producto;

public class DTProducto implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String nombre;
	private String descripcion;
	private double precio;
	private String foto;
	private int descuento;
	private boolean activo;
	private DTRestaurante restaurante;
	private List<DTCategoria> categorias;

	public DTProducto() {
		super();
	}

	public DTProducto(int id, String nombre, String descripcion, double precio, String foto, int descuento,
			boolean activo) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.foto = foto;
		this.descuento = descuento;
		this.activo = activo;
	}

	public DTProducto(Producto pro) {
		this.id = pro.getId();
		this.nombre = pro.getNombre();
		this.descripcion = pro.getDescripcion();
		this.precio = pro.getPrecio();
		this.foto = pro.getFoto();
		this.descuento = pro.getDescuento();
		this.activo = pro.isActivo();

		for (Categoria cat : pro.getCategorias()) {
			this.categorias.add(new DTCategoria(cat));
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public int getDescuento() {
		return descuento;
	}

	public void setDescuento(int descuento) {
		this.descuento = descuento;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public DTRestaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(DTRestaurante restaurante) {
		this.restaurante = restaurante;
	}

	public List<DTCategoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<DTCategoria> categorias) {
		this.categorias = categorias;
	}
}