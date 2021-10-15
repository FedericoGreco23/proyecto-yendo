package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class DTPromocion implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// No tiene mucho sentido poner el DT aca
    private List<String> productos = new ArrayList<>();
	
	public DTPromocion() {
		super();
	}

	public DTPromocion(List<String> productos) {
        this.productos = productos;
    }

	public List<String> getProductos() {
		return productos;
	}

	public void setProductos(List<String> productos) {
		this.productos = productos;
	}
}