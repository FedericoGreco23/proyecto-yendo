package com.vpi.springboot.Modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Promocion extends Producto {

	@ManyToMany
	@JoinTable(name = "producto_promocion")
	private List<Producto> productos = new ArrayList<>();

	public Promocion(String nombre, String descripcion, double precio, String foto, int descuento, boolean activo) {
		super(nombre, descripcion, precio, foto, descuento, activo);
	}

	public Promocion() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

}
