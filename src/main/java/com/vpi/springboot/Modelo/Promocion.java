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

	public Promocion(int id, String nombre, String descripcion, double precio, String foto, int descuento,
			boolean activo) {
		super(id, nombre, descripcion, precio, foto, descuento, activo);
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

}
