package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vpi.springboot.Modelo.Categoria;
import com.vpi.springboot.Modelo.Producto;

public class DTCategoriaProducto implements Serializable {
	private static final long serialVersionUID = 1L;

	private DTCategoria categoria;
	private List<DTProducto> productos = new ArrayList<>();
	
	public DTCategoriaProducto() {
		super();
	}

	public DTCategoriaProducto(Categoria categoria, List<Producto> productos) {
		this.categoria = new DTCategoria(categoria);
		if(productos.size() > 0) {
			for(Producto p : productos) {
				this.productos.add(new DTProducto(p));
			}
		}
	}

	public DTCategoria getCategoria() {
		return categoria;
	}

	public void setCategoria(DTCategoria categoria) {
		this.categoria = categoria;
	}

	public List<DTProducto> getProductos() {
		return productos;
	}

	public void setProductos(List<DTProducto> productos) {
		this.productos = productos;
	}
}